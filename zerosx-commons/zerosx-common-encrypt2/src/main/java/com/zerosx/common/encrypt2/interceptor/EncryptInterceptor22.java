package com.zerosx.common.encrypt2.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zerosx.common.encrypt2.anno.EncryptClass;
import com.zerosx.common.encrypt2.anno.EncryptField;
import com.zerosx.common.encrypt2.anno.EncryptKeys;
import com.zerosx.common.encrypt2.core.EncryptorCacheManager;
import com.zerosx.common.encrypt2.core.ICustomEncryptor;
import com.zerosx.common.encrypt2.utils.MybatisEncryptUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询参数加密拦截器
 */
@Slf4j
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),})
public class EncryptInterceptor22 implements Interceptor {

    private static final String PAGE_COUNT = "_mpCount";

    private final EncryptorCacheManager encryptorCacheManager;

    public EncryptInterceptor22(EncryptorCacheManager encryptorCacheManager) {
        this.encryptorCacheManager = encryptorCacheManager;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        Object paramObject = parameterHandler.getParameterObject();

        Field parameterField = parameterHandler.getClass().getDeclaredField("mappedStatement");
        parameterField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) parameterField.get(parameterHandler);
        String namespace = MybatisEncryptUtils.getNamespace(mappedStatement);
        Method method = MybatisEncryptUtils.getMethodByNamespace(namespace);
        //自定义mapper方法
        if (Objects.nonNull(method)) {
            EncryptKeys encryptKey = method.getAnnotation(EncryptKeys.class);
            //没有需要加密的
            if (Objects.isNull(encryptKey)) {
                return invocation.proceed();
            }
            if (paramObject instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) paramObject;
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    String key = entry.getKey();
                    Object paramValue = entry.getValue();
                    if (!key.startsWith("param") && Objects.nonNull(paramValue)) {
                        Object targetObj = entry.getValue();
                        if (targetObj instanceof String) {

                        }

                        Class<?> targetObjClass = targetObj.getClass();


                        Pair<Class<?>, Object> pair = handleQueryWrapper(targetObj);

                        boolean isQueryWrapperOrLambdaQueryWrapper = Objects.nonNull(pair);
                        //是AbstractWrapper类型
                        if (isQueryWrapperOrLambdaQueryWrapper) {
                            Class<?> wrapperClz = pair.getLeft();
                            if (wrapperClz == null) {
                                log.warn("数据加解密类型为空:{}", targetObjClass.getName());
                                return invocation.proceed();
                            }
                            Object entity = pair.getRight();
                            if (entity == null) {
                                AbstractWrapper wrapper = (AbstractWrapper) targetObj;
                                Map<String, Object> valuePairs = wrapper.getParamNameValuePairs();
                                Field paramNameValuePairs = AbstractWrapper.class.getDeclaredField("paramNameValuePairs");
                                String regex = wrapper.getParamAlias() + "." + paramNameValuePairs.getName() + ".";
                                String sqlSegment = wrapper.getExpression().getSqlSegment().replaceAll(regex, "");
                                Map<String, String> map = resolve(sqlSegment);
                                if (!map.isEmpty()) {
                                    for (Map.Entry<String, String> entry1 : map.entrySet()) {
                                        Object value = valuePairs.get(entry1.getKey());
                                        if (value != null) {
                                            EncryptField encryptField = ReflectUtil.getField(wrapperClz, StrUtil.toCamelCase(entry1.getValue())).getAnnotation(EncryptField.class);
                                            if (Objects.nonNull(encryptField)) {
                                                if (value instanceof String) {
                                                    String strValue = (String) value;
                                                    String strValueTemp = strValue.replaceAll("%", "");
                                                    valuePairs.put(entry1.getKey(), strValue.replace(strValueTemp, encryptValue(strValueTemp)));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                //是否敏感类
                                EncryptClass encryptData = AnnotationUtils.findAnnotation(wrapperClz, EncryptClass.class);
                                if (encryptData == null) {
                                    return invocation.proceed();
                                }
                                //获取敏感字段
                                Set<Field> encryptClzFields = encryptorCacheManager.getEncryptClzFields(wrapperClz);
                                if (CollectionUtils.isEmpty(encryptClzFields)) {
                                    return invocation.proceed();
                                }
                                for (Field fs : encryptClzFields) {
                                    Object fieldValue = ReflectUtil.getFieldValue(entity, fs);
                                    if (fieldValue instanceof String) {
                                        String fieldValueStr = (String) fieldValue;
                                        ReflectUtil.setFieldValue(entity, fs, encryptValue(fieldValueStr));
                                    }
                                }
                                ReflectUtil.setFieldValue(targetObj, "entity", entity);
                            }
                        }
                    }
                }
            }
        } else {
            //todo
        }
        return invocation.proceed();
    }

    private static Map<String, String> resolve(String line) {
        Map<String, String> map = new HashMap<>();
        //String line = "(mobile = #{MPGENVAL1} AND status LIKE #{MPGENVAL2} AND id IN (#{MPGENVAL3},#{MPGENVAL4},#{MPGENVAL5}) AND ((id = #{MPGENVAL6}))) GROUP BY id HAVING id > #{MPGENVAL7} ORDER BY add_blacklist_time DESC";
        String syntax = "(?:=|!=|LIKE|NOT LIKE|IN|NOT IN|>|<|<=|>=|<>)";
        String pattern = "(\\w+\\s*" + syntax + "\\s*[\\w\\S]*)(\\#\\{\\w+\\})";
        Matcher m = Pattern.compile(pattern).matcher(line);
        while (m.find()) {
            String syntaxLine = m.group(0).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("#", "").replaceAll("\\{", "").replaceAll("\\}", "");
            Matcher m2 = Pattern.compile(syntax).matcher(syntaxLine);
            if (m2.find()) {
                String[] syntaxArray = syntaxLine.split(m2.group(0));
                Arrays.stream(syntaxArray[1].split(",")).forEach(v -> map.put(v.trim(), syntaxArray[0].trim()));
            }
        }
        return map;
    }

    public String encryptValue(String srcValue) {
        if (StringUtils.isBlank(srcValue)) {
            return srcValue;
        }
        ICustomEncryptor encryptor = encryptorCacheManager.getEncryptor();
        String encryptValue = encryptor.encrypt(srcValue);
        log.debug("加密字段值:{} = {}", srcValue, encryptValue);
        return encryptValue;
    }

    @SneakyThrows
    private Pair<Class<?>, Object> handleQueryWrapper(Object targetObj) {
        if (targetObj instanceof LambdaQueryWrapper) {
            return Pair.of(((LambdaQueryWrapper<?>) targetObj).getEntityClass(), ((LambdaQueryWrapper<?>) targetObj).getEntity());
        }
        if (targetObj instanceof LambdaUpdateWrapper) {
            return Pair.of(((LambdaUpdateWrapper<?>) targetObj).getEntityClass(), ((LambdaUpdateWrapper<?>) targetObj).getEntity());
        }
        if (targetObj instanceof QueryWrapper) {
            return Pair.of(((QueryWrapper<?>) targetObj).getEntityClass(), ((QueryWrapper<?>) targetObj).getEntity());
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
