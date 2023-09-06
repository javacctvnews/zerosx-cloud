package com.zerosx.common.redis.templete;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate操作工具类
 * 不使用这个类进行redis操作
 */
@Deprecated
public class RedisOpService {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisOpService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * getConnectionFactory
     *
     * @return
     */
    public RedisConnectionFactory getRedisConnectionFactory() {
        return redisTemplate.getConnectionFactory();
    }

    /**
     * 添加到缓存
     *
     * @param key
     * @param value
     */
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据key获取对象
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key获取对象
     *
     * @param key             the key
     * @param valueSerializer 序列化
     * @return the string
     */
    public Object get(final String key, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key, valueSerializer);
        return redisTemplate.execute(connection -> deserializeValue(connection.get(rawKey), valueSerializer), true);
    }

    private byte[] rawKey(Object key, RedisSerializer<Object> valueSerializer) {
        Assert.notNull(key, "non null key required");

        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        return valueSerializer.serialize(key);
    }

    private Object deserializeValue(byte[] value, RedisSerializer<Object> valueSerializer) {
        if (valueSerializer == null) {
            return value;
        }
        return valueSerializer.deserialize(value);
    }

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the byte [ ]
     */
    public byte[] get(final byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public boolean del(final String key) {
        Assert.notNull(key, "non null key required");
        return redisTemplate.delete(key);
    }

    /**
     * 删除key
     *
     * @param keys the keys
     * @return the long
     */
    public boolean del(final String... keys) {
        boolean result = false;
        for (String key : keys) {
            del(key);
        }
        return result;
    }

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   key
     * @param value 值
     * @param time  过期时间,单位：秒
     */
    public void setExpire(final String key, final Object value, final long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public Object getHashValues(String key, String hashKey) {
        return opsForHash().get(key, hashKey);
    }

    /**
     * 根据key值删除
     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    public void delHashValues(String key, Object... hashKeys) {
        opsForHash().delete(key, hashKeys);
    }

    /**
     * Ops for hash hash operations.
     *
     * @return the hash operations
     */
    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public void putHashValue(String key, String hashKey, Object hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * redis List 引擎
     *
     * @return the list operations
     */
    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     *
     * @param key the key
     * @return the long
     */
    public Long length(String key) {
        return opsForList().size(key);
    }

    public Long length(byte[] key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.lLen(key));
    }

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public List<Object> getList(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    public List<byte[]> getByteList(byte[] key, int start, int end) {
        return redisTemplate.execute(connection -> connection.lRange(key, start, end), true);
    }

    public Long lRem(byte[] key, long i, byte[] value) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.lRem(key, i, value));
    }

}