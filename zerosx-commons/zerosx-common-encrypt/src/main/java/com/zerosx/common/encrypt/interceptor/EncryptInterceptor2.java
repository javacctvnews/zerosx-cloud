//package com.zerosx.common.encrypt.interceptor;
//
//import cn.hutool.core.util.ReflectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.zerosx.common.encrypt.anno.EncryptClz;
//import com.zerosx.common.encrypt.anno.EncryptField;
//import com.zerosx.common.encrypt.core.EncryptorCacheManager;
//import com.zerosx.common.encrypt.core.ICustomEncryptor;
//import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.tuple.Pair;
//import org.apache.ibatis.binding.MapperMethod;
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.sql.PreparedStatement;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Slf4j
//@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),})
//public class EncryptInterceptor2 implements Interceptor {
//
//    private final EncryptorCacheManager encryptorCacheManager;
//    private final CustomEncryptProperties customEncryptProperties;
//
//    public EncryptInterceptor2(EncryptorCacheManager encryptorCacheManager, CustomEncryptProperties customEncryptProperties) {
//        this.encryptorCacheManager = encryptorCacheManager;
//        this.customEncryptProperties = customEncryptProperties;
//    }
//
//    /**
//     * 类上的@Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler,若指定ResultSetHandler ，这里则能强转为ResultSetHandler
//     *
//     * @param invocation
//     * @return
//     * @throws Throwable
//     */
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
//        this.encrypt2(parameterHandler);
//        return invocation.proceed();
//    }
//
//    private void encrypt2(ParameterHandler parameterHandler) throws NoSuchFieldException, ClassNotFoundException {
//        Object parameterObject = parameterHandler.getParameterObject();
//        if (Objects.isNull(parameterObject)) {
//            return;
//        }
//        if (parameterObject instanceof MapperMethod.ParamMap) {
//            MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) parameterObject;
//            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
//                String key = entry.getKey();
//                Object paramValue = entry.getValue();
//                if (!key.startsWith("param") && Objects.nonNull(paramValue)) {
//                    encrypt3(parameterHandler, entry);
//                }
//            }
//            return;
//        }
//        handleMethodNotInMapper(parameterObject);
//    }
//
//    @SneakyThrows
//    private void handleMethodNotInMapper(Object targetObj) {
//        if (Objects.isNull(targetObj)) {
//            return;
//        }
//        Class<?> targetObjClass = targetObj.getClass();
//
//        Pair<Class<?>, Object> pair = handleQueryWrapper(targetObj);
//        boolean isQueryWrapperOrLambdaQueryWrapper = Objects.nonNull(pair);
//        //是AbstractWrapper类型
//        if (isQueryWrapperOrLambdaQueryWrapper) {
//            Class<?> wrapperClz = pair.getLeft();
//            if (wrapperClz == null) {
//                log.warn("数据加解密类型为空:{}", targetObjClass.getName());
//                return;
//            }
//            Object entity = pair.getRight();
//            if (entity == null) {
//                AbstractWrapper wrapper = (AbstractWrapper) targetObj;
//                Map<String, Object> valuePairs = wrapper.getParamNameValuePairs();
//                Field paramNameValuePairs = AbstractWrapper.class.getDeclaredField("paramNameValuePairs");
//                String regex = wrapper.getParamAlias() + "." + paramNameValuePairs.getName() + ".";
//                String sqlSegment = wrapper.getExpression().getSqlSegment().replaceAll(regex, "");
//                Map<String, String> map = resolve(sqlSegment);
//                if (!map.isEmpty()) {
//                    for (Map.Entry<String, String> entry : map.entrySet()) {
//                        Object value = valuePairs.get(entry.getKey());
//                        if (value != null) {
//                            EncryptField encryptField = ReflectUtil.getField(wrapperClz, StrUtil.toCamelCase(entry.getValue())).getAnnotation(EncryptField.class);
//                            if (Objects.nonNull(encryptField)) {
//                                if (value instanceof String) {
//                                    String strValue = (String) value;
//                                    String strValueTemp = strValue.replaceAll("%", "");
//                                    valuePairs.put(entry.getKey(), strValue.replace(strValueTemp, encryptValue(strValueTemp)));
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                //是否敏感类
//                EncryptClz encryptData = AnnotationUtils.findAnnotation(wrapperClz, EncryptClz.class);
//                if (encryptData == null) {
//                    return;
//                }
//                //获取敏感字段
//                Set<Field> encryptClzFields = encryptorCacheManager.getEncryptClzFields(wrapperClz);
//                if (CollectionUtils.isEmpty(encryptClzFields)) {
//                    return;
//                }
//                for (Field fs : encryptClzFields) {
//                    Object fieldValue = ReflectUtil.getFieldValue(entity, fs);
//                    if (fieldValue instanceof String) {
//                        String fieldValueStr = (String) fieldValue;
//                        ReflectUtil.setFieldValue(entity, fs, encryptValue(fieldValueStr));
//                    }
//                }
//                ReflectUtil.setFieldValue(targetObj, "entity", entity);
//            }
//        }
//        //是否敏感类
//        EncryptClz encryptData = AnnotationUtils.findAnnotation(targetObjClass, EncryptClz.class);
//        if (encryptData == null) {
//            return;
//        }
//        //获取敏感字段
//        Set<Field> encryptClzFields = encryptorCacheManager.getEncryptClzFields(targetObjClass);
//        if (CollectionUtils.isEmpty(encryptClzFields)) {
//            return;
//        }
//        for (Field fs : encryptClzFields) {
//            Object fieldValue = ReflectUtil.getFieldValue(targetObj, fs);
//            if (fieldValue instanceof String) {
//                String fieldValueStr = (String) fieldValue;
//                ReflectUtil.setFieldValue(targetObj, fs, encryptValue(fieldValueStr));
//            }
//        }
//    }
//
//    @SneakyThrows
//    private Pair<Class<?>, Object> handleQueryWrapper(Object targetObj) {
//        if (targetObj instanceof LambdaQueryWrapper) {
//            return Pair.of(((LambdaQueryWrapper<?>) targetObj).getEntityClass(), ((LambdaQueryWrapper<?>) targetObj).getEntity());
//        }
//        if (targetObj instanceof LambdaUpdateWrapper) {
//            return Pair.of(((LambdaUpdateWrapper<?>) targetObj).getEntityClass(), ((LambdaUpdateWrapper<?>) targetObj).getEntity());
//        }
//        if (targetObj instanceof QueryWrapper) {
//            return Pair.of(((QueryWrapper<?>) targetObj).getEntityClass(), ((QueryWrapper<?>) targetObj).getEntity());
//        }
//        return null;
//    }
//
//    private static Map<String, String> resolve(String line) {
//        Map<String, String> map = new HashMap<>();
//        //String line = "(mobile = #{MPGENVAL1} AND status LIKE #{MPGENVAL2} AND id IN (#{MPGENVAL3},#{MPGENVAL4},#{MPGENVAL5}) AND ((id = #{MPGENVAL6}))) GROUP BY id HAVING id > #{MPGENVAL7} ORDER BY add_blacklist_time DESC";
//        String syntax = "(?:=|!=|LIKE|NOT LIKE|IN|NOT IN|>|<|<=|>=|<>)";
//        String pattern = "(\\w+\\s*" + syntax + "\\s*[\\w\\S]*)(\\#\\{\\w+\\})";
//        Matcher m = Pattern.compile(pattern).matcher(line);
//        while (m.find()) {
//            String syntaxLine = m.group(0).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("#", "").replaceAll("\\{", "").replaceAll("\\}", "");
//            Matcher m2 = Pattern.compile(syntax).matcher(syntaxLine);
//            if (m2.find()) {
//                String[] syntaxArray = syntaxLine.split(m2.group(0));
//                Arrays.stream(syntaxArray[1].split(",")).forEach(v -> map.put(v.trim(), syntaxArray[0].trim()));
//            }
//        }
//        return map;
//    }
//
//    private void encrypt3(ParameterHandler parameterHandler, Map.Entry<String, Object> entry) throws NoSuchFieldException, ClassNotFoundException {
//        Object paramValue = entry.getValue();
//        //字符串
//        if (paramValue instanceof String) {
//            encryptStringParam(parameterHandler, entry);
//            return;
//        }
//        //请求参数不是字符串
//        handleMethodNotInMapper(paramValue);
//    }
//
//
//    /**
//     * 加密String类型的参数
//     */
//    private void encryptStringParam(ParameterHandler parameterHandler, Map.Entry<String, Object> entry) throws NoSuchFieldException, ClassNotFoundException {
//        Class<? extends ParameterHandler> aClass = parameterHandler.getClass();
//        Field mappedStatement = aClass.getDeclaredField("mappedStatement");
//        ReflectUtil.setAccessible(mappedStatement);
//        MappedStatement statement = (MappedStatement) ReflectUtil.getFieldValue(parameterHandler, mappedStatement);
//        //方法命名空间
//        String nameSpace = statement.getId();
//        if (StringUtils.isBlank(nameSpace)) {
//            return;
//        }
//        String methodName = nameSpace.substring(nameSpace.lastIndexOf(".") + 1);
//        String className = nameSpace.substring(0, nameSpace.lastIndexOf("."));
//
//        Method[] ms = Class.forName(className).getMethods();
//        Optional<Method> optionalMethod = Arrays.stream(ms).filter(item -> StringUtils.equals(item.getName(), methodName)).findFirst();
//        if (!optionalMethod.isPresent()) {
//            return;
//        }
//        Method method = optionalMethod.get();
//        ReflectUtil.setAccessible(method);
//        //方法参数里面的请求参数注解列表
//        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//        //只要参数中的有EncryptField注解（参数注解可能有多个）则加密
//        for (Annotation[] parameterAnnotation : parameterAnnotations) {
//            if (Arrays.stream(parameterAnnotation).anyMatch(anno -> anno instanceof EncryptField)) {
//                entry.setValue(encryptValue((String) entry.getValue()));
//            }
//        }
//    }
//
//    public String encryptValue(String srcValue) {
//        if (StringUtils.isBlank(srcValue)) {
//            return srcValue;
//        }
//        ICustomEncryptor encryptor = EncryptorCacheManager.getEncryptor(customEncryptProperties);
//        String encryptValue = encryptor.encrypt(srcValue);
//        log.debug("加密字段值:{} = {}", srcValue, encryptValue);
//        return encryptValue;
//    }
//
//
//    //拦截器链
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    //自定义配置文件操作
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//
//}
