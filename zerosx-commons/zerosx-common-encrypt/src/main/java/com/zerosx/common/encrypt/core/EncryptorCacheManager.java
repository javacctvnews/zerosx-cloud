package com.zerosx.common.encrypt.core;

import cn.hutool.core.util.ReflectUtil;
import com.zerosx.common.encrypt.anno.EncryptClass;
import com.zerosx.common.encrypt.anno.EncryptField;
import com.zerosx.common.encrypt.enums.AlgorithmEnum;
import com.zerosx.common.encrypt.properties.CustomEncryptProperties;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EncryptorCacheManager {

    private final CustomEncryptProperties customEncryptProperties;

    public EncryptorCacheManager(CustomEncryptProperties customEncryptProperties) {
        this.customEncryptProperties = customEncryptProperties;
    }


    /**
     * 类加密字段缓存
     */
    Map<Class<?>, Set<Field>> encryptCacheClz = new ConcurrentHashMap<>();
    /**
     * 加解密实现缓存
     */
    private static Map<Class<? extends ICustomEncryptor>, ICustomEncryptor> encryptorMap = new ConcurrentHashMap<>();

    /**
     * 获取需加密类的字段
     *
     * @param clz
     * @return
     */
    public Set<Field> getEncryptClzFields(Class<?> clz) {
        //缓存
        Set<Field> fields = encryptCacheClz.get(clz);
        if (CollectionUtils.isNotEmpty(fields)) {
            return fields;
        }
        //是否有加密注解
        EncryptClass encryptClass = clz.getAnnotation(EncryptClass.class);
        if (encryptClass == null) {
            return new HashSet<>();
        }
        Field[] declaredFields = clz.getDeclaredFields();
        Set<Field> encryptFields = Arrays.stream(declaredFields).filter(field -> field.getAnnotation(EncryptField.class) != null && field.getType() == String.class).collect(Collectors.toSet());
        for (Field encryptField : encryptFields) {
            encryptField.setAccessible(true);
        }
        encryptCacheClz.put(clz, encryptFields);
        return encryptFields;
    }

    public ICustomEncryptor getEncryptor() {
        Class<? extends ICustomEncryptor> algorithm = AlgorithmEnum.getAlgorithm(customEncryptProperties.getAlgorithm());
        return getEncryptor(algorithm);
    }

    public ICustomEncryptor getEncryptor(Class<? extends ICustomEncryptor> algorithmClz) {
        ICustomEncryptor encryptor = encryptorMap.get(algorithmClz);
        if (encryptor != null) {
            return encryptor;
        }
        ICustomEncryptor customEncryptor = ReflectUtil.newInstance(algorithmClz, customEncryptProperties.getKey());
        encryptorMap.put(algorithmClz, customEncryptor);
        return customEncryptor;
    }

}
