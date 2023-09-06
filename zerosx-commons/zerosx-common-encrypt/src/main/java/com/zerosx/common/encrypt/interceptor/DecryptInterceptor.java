package com.zerosx.common.encrypt.interceptor;

import com.zerosx.common.encrypt.anno.EncryptField;
import com.zerosx.common.encrypt.core.EncryptorCacheManager;
import com.zerosx.common.encrypt.core.ICustomEncryptor;
import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class DecryptInterceptor implements Interceptor {

    private final EncryptorCacheManager encryptorCacheManager;
    private final CustomEncryptProperties customEncryptProperties;

    public DecryptInterceptor(EncryptorCacheManager encryptorCacheManager, CustomEncryptProperties customEncryptProperties) {
        this.encryptorCacheManager = encryptorCacheManager;
        this.customEncryptProperties = customEncryptProperties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //取出查询的结果
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)) {
            return null;
        }
        decrypt(resultObject);
        return resultObject;
    }

    private void decrypt(Object sourceObject) {
        if (Objects.isNull(sourceObject)) {
            return;
        }
        if (sourceObject instanceof Map<?, ?>) {
            Map<?, ?> sourceMap = (HashMap<?, ?>) sourceObject;
            //循环
            sourceMap.values().forEach(this::decrypt);
            return;
        }
        if (sourceObject instanceof List<?>) {
            List<?> sourceList = (List<?>) sourceObject;
            if (CollectionUtils.isEmpty(sourceList)) {
                return;
            }
            // 判断第一个元素是否含有注解。如果没有直接返回，提高效率
            Object firstItem = sourceList.get(0);
            if (Objects.isNull(firstItem) || CollectionUtils.isEmpty(encryptorCacheManager.getEncryptClzFields(firstItem.getClass()))) {
                return;
            }
            //循环
            ((List<?>) sourceObject).forEach(this::decrypt);
            return;
        }
        ICustomEncryptor encryptor;
        Object object;
        EncryptField encryptField;
        String decryptedValue;
        Set<Field> fields = encryptorCacheManager.getEncryptClzFields(sourceObject.getClass());
        for (Field field : fields) {
            try {
                encryptField = field.getAnnotation(EncryptField.class);
                encryptor = encryptorCacheManager.getEncryptor(encryptField.algo());
                object = field.get(sourceObject);
                decryptedValue = encryptor.decrypt(String.valueOf(object));
                field.set(sourceObject, decryptedValue);
                //log.debug("待解密字段:{} 解密前：{} 解密后：{}", field.getName(), object, decryptedValue);
            } catch (IllegalAccessException e) {
                log.error("字段解密异常：" + field.getName(), e);
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
