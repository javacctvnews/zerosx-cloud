package com.zerosx.common.security.utils;

import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

/**
 * 处理token存储的key/value的序列化和反序列化
 * 因为 `CustomRedisTokenStore.java` 使用的是`RedisTokenStoreSerializationStrategy.java`
 */
public class JdkSerializationUtils {

    private static final RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    /**
     * 序列化
     *
     * @param keyString
     * @return
     */
    public static byte[] serialize(String keyString) {
        return serializationStrategy.serialize(keyString);
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }


    /**
     * 反序列化
     * @param data
     * @param clz
     * @return
     * @param <T>
     */
    public static <T> T deserialize(byte[] data, Class<T> clz) {
        return serializationStrategy.deserialize(data, clz);
    }

    /**
     * 反序列化
     * @param data
     * @return
     */
    public static String deserialize(byte[] data) {
        return serializationStrategy.deserializeString(data);
    }
}
