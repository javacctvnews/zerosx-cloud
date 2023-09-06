package com.zerosx.common.encrypt.interceptor;

import com.zerosx.common.encrypt.anno.EncryptField;
import com.zerosx.common.encrypt.core.EncryptorCacheManager;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),})
public class EncryptInterceptor implements Interceptor {

    private final EncryptorCacheManager encryptorCacheManager;
    private final CustomEncryptProperties customEncryptProperties;

    public EncryptInterceptor(EncryptorCacheManager encryptorCacheManager, CustomEncryptProperties customEncryptProperties) {
        this.encryptorCacheManager = encryptorCacheManager;
        this.customEncryptProperties = customEncryptProperties;
    }

    /**
     * 类上的@Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler,若指定ResultSetHandler ，这里则能强转为ResultSetHandler
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        Object parameterObject = parameterField.get(parameterHandler);
        if (parameterObject != null) {
            encrypt(parameterObject);
        }
        return invocation.proceed();
    }

    private void encrypt(Object parameterObject) {
        if (Objects.isNull(parameterObject)) {
            return;
        }
        //log.debug("参数clz:{} : {}", parameterObject.getClass().getName(), parameterObject);
        if (parameterObject instanceof Map<?, ?>) {
            new HashSet<>(((Map<?, ?>) parameterObject).values()).forEach(this::encrypt);
            return;
        }
        if (parameterObject instanceof List<?>) {
            List<?> sourceList = (List<?>) parameterObject;
            if (CollectionUtils.isEmpty(sourceList)) {
                return;
            }
            // 判断第一个元素是否含有注解。如果没有直接返回，提高效率
            Object firstItem = sourceList.get(0);
            if (Objects.isNull(firstItem) || CollectionUtils.isEmpty(encryptorCacheManager.getEncryptClzFields(firstItem.getClass()))) {
                return;
            }
            ((List<?>) parameterObject).forEach(this::encrypt);
            return;
        }
        //从缓存中获取
        Set<Field> fields = encryptorCacheManager.getEncryptClzFields(parameterObject.getClass());
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        Object object;
        EncryptField encryptField;
        String encryptValue;
        ICustomEncryptor encryptor;
        for (Field field : fields) {
            try {
                encryptField = field.getAnnotation(EncryptField.class);
                encryptor = encryptorCacheManager.getEncryptor(encryptField.algo());
                object = field.get(parameterObject);
                encryptValue = encryptor.encrypt(String.valueOf(object));
                field.set(parameterObject, encryptValue);
                //log.debug("待加密字段:{} 加密前：{} 加密后：{}", field.getName(), object, encryptValue);
            } catch (IllegalAccessException e) {
                log.error("处理加密字段时出错" + field.getName(), e);
            }
        }
    }

    //拦截器链
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    //自定义配置文件操作
    @Override
    public void setProperties(Properties properties) {

    }


}
