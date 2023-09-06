package com.zerosx.common.redis.templete;

import org.redisson.api.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redisson操作工具，推荐使用
 */
public class RedissonOpService {

    private final RedissonClient redissonClient;

    public RedissonOpService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * set 添加到缓存
     */
    public <T> void set(final String key, final T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T get(final String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }

    /**
     * set 添加到缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间，秒
     * @param <T>        T
     */
    public <T> void setExpire(final String key, final T value, final long expireTime) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 删除key并返回值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T delAndGet(final String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.getAndDelete();
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public boolean del(final String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.delete();
    }

    /**
     * 批量删除key
     *
     * @param keys
     * @return
     */
    public boolean del(final String... keys) {
        for (String key : keys) {
            del(key);
        }
        return true;
    }


    /**
     * Hash操作 set
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public <T> void putHashValue(String key, String hashKey, T hashValue) {
        RMap<String, T> map = redissonClient.getMap(key);
        map.put(hashKey, hashValue);
    }

    /**
     * Hash操作 get
     *
     * @param key     the key
     * @param hashKey the hash key
     */
    public <T> T getHashValue(final String key, final String hashKey) {
        RMap<String, T> map = redissonClient.getMap(key);
        return map.get(hashKey);
    }

    /**
     * Hash操作 del
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T delHashValues(final String key, final String hKey) {
        RMap<String, T> rMap = redissonClient.getMap(key);
        return rMap.remove(hKey);
    }


    /**
     * 按前缀删除
     *
     * @param pattern key前缀
     */
    public void deleteKeys(final String pattern) {
        redissonClient.getKeys().deleteByPattern(pattern);
    }

    /**
     * 是否存在key
     *
     * @param key 键
     */
    public Boolean existKey(final String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.countExists(key) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> boolean putListValues(final String key, final List<T> dataList) {
        RList<T> rList = redissonClient.getList(key);
        return rList.addAll(dataList);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getListValues(final String key) {
        RList<T> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getRangeValues(final String key, final int beginIndex, final int endIndex) {
        RList<T> rList = redissonClient.getList(key);
        return rList.range(beginIndex, endIndex);
    }

    /**
     * @param key the key
     * @return the long
     */
    public Integer getListSize(final String key) {
        RList<Object> list = redissonClient.getList(key);
        return list.size();
    }


    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> boolean putSets(final String key, final Set<T> dataSet) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.addAll(dataSet);
    }


    /**
     * 缓存Set
     *
     * @param key
     * @param data
     * @return
     */
    public boolean putSet(final String key, final Object data) {
        RSet<Object> rSet = redissonClient.getSet(key);
        return rSet.add(data);
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的key
     * @return set对象
     */
    public <T> Set<T> getSet(final String key) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.readAll();
    }


    public boolean delSet(String key, Object value) {
        RSet<Object> rSet = redissonClient.getSet(key);
        return rSet.remove(value);
    }

}
