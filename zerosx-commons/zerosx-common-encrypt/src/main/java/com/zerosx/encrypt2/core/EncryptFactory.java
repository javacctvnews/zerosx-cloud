package com.zerosx.encrypt2.core;

import cn.hutool.core.util.ReflectUtil;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.anno.EncryptFields;
import com.zerosx.encrypt2.core.encryptor.IEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * EncryptFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-14 13:12
 **/
@Slf4j
public abstract class EncryptFactory {

    /**
     * 类加密字段缓存
     */
    private static final Map<Class<?>, Set<Field>> encryptCacheClz = new ConcurrentHashMap<>();

    /**
     * 算法实例缓存
     * key: 算法加密码
     * value：算法加密的实例
     */
    private static final Map<String, IEncryptor> encryptorMap = new ConcurrentHashMap<>();

    /**
     * 创建指定算法的IEncryptor实例
     *
     * @param algorithmClz
     * @param password
     * @return
     */
    public static IEncryptor getEncryptIfAbsent(Class<? extends IEncryptor> algorithmClz, String password) {
        String key = algorithmClz.getName() + ":" + password;
        if (encryptorMap.containsKey(key)) {
            return encryptorMap.get(key);
        }
        IEncryptor encryptor = ReflectUtil.newInstance(algorithmClz, password);
        log.debug("初始化IEncryptor实例:{} {} 并缓存实例", algorithmClz, password);
        encryptorMap.put(key, encryptor);
        return encryptor;
    }

    /**
     * 获取需加密类的字段
     *
     * @param clz
     * @return
     */
    public static boolean containsEncrypt(Class<?> clz) {
        //是否有加密注解
        EncryptClass encryptClass = clz.getAnnotation(EncryptClass.class);
        return encryptClass != null;
    }

    /**
     * 获取需加密类的字段
     *
     * @param clz 类
     * @return
     */
    public static Set<Field> getEncryptFields(Class<?> clz) {
        //是否有加密注解
        EncryptClass encryptClass = clz.getAnnotation(EncryptClass.class);
        if (encryptClass == null) {
            return new HashSet<>();
        }
        //缓存
        Set<Field> fields = encryptCacheClz.get(clz);
        if (CollectionUtils.isNotEmpty(fields)) {
            return fields;
        }
        Field[] declaredFields = clz.getDeclaredFields();
        Set<Field> encryptFields = Arrays.stream(declaredFields).filter(e -> {
            EncryptField encryptField = e.getAnnotation(EncryptField.class);
            if (encryptField != null) {
                e.setAccessible(true);
                return true;
            }
            EncryptFields encryptKeys = e.getAnnotation(EncryptFields.class);
            if (encryptKeys != null) {
                e.setAccessible(true);
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
        encryptCacheClz.put(clz, encryptFields);
        return encryptFields;
    }

}
