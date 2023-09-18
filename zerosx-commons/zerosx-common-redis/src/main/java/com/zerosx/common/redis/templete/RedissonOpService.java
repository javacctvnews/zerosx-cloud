package com.zerosx.common.redis.templete;

import lombok.Getter;
import org.redisson.api.*;
import org.redisson.client.protocol.ScoredEntry;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redisson客户端的操作工具，推荐使用
 */
@Getter
public class RedissonOpService {

    private static final long MILLI_SECONDS = 1000;
    /**
     * key默认过期时间
     */
    private static final long DEFAULT_EXPIRE_SECONDS = 7 * 24 * 3600;
    private static final Duration DEFAULT_EXPIRE_DURATION = Duration.ofSeconds(DEFAULT_EXPIRE_SECONDS);

    /**
     * -- GETTER --
     * 获取RedissonClient
     *
     * @return RedissonClient
     */
    private final RedissonClient redissonClient;

    public RedissonOpService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 添加到缓存
     */
    public <V> void set(final String key, final V value) {
        set(key, value, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 添加到缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间，秒
     * @param <V>        V
     */
    public <V> void set(final String key, final V value, final long expireTime) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        bucket.set(value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 不存在时设值
     * Params:
     * value – value to set
     * duration – expiration duration
     * Returns: true if successful, or false if element was already set
     */
    public <V> boolean setIfAbsent(final String key, final V value, final long expireTime) {
        return redissonClient.getBucket(key).setIfAbsent(value, Duration.ofSeconds(expireTime));
    }

    /**
     * 不存在时设值
     * Params:
     * value – value to set
     * duration – expiration duration
     * Returns: true if successful, or false if element was already set
     */
    public <V> boolean setIfAbsent(final String key, final V value) {
        return setIfAbsent(key, value, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <V> V get(final String key) {
        RBucket<V> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }


    /**
     * 获取key的过期时间，单位：秒
     * time in milliseconds -2 if the key does not exist. -1 if the key exists but has no associated expire.
     *
     * @param key key
     * @return 过期时间，单位：秒，-2表示key不存在(已过期)，-1表示key存在但是未关联过期时间
     */
    public long expireTime(final String key) {
        //毫秒
        long timeToLive = redissonClient.getBucket(key).remainTimeToLive();
        if (timeToLive > 0) {
            return timeToLive / MILLI_SECONDS;
        }
        return timeToLive;
    }

    /**
     * 删除key并返回key存储的内容
     *
     * @param key
     * @param <V>
     * @return
     */
    public <V> V getAndDelete(final String key) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        return bucket.getAndDelete();
    }

    /**
     * 删除key (通用)
     *
     * @param key
     * @return
     */
    public <V> boolean del(final String key) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        return bucket.delete();
    }

    /**
     * 批量删除key (通用)
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
     * hash结构-put操作
     *
     * @param key       key
     * @param hashKey   hash key
     * @param hashValue hash value
     */
    public <V> void hPut(final String key, final String hashKey, final V hashValue) {
        hPut(key, hashKey, hashValue, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * hash结构-put操作
     *
     * @param key        key
     * @param hashKey    hash key
     * @param hashValue  hash value
     * @param expireTime 过期时间，单位：秒。注意：这个过期时间是整个key的过期时间
     */
    public <V> void hPut(final String key, final String hashKey, final V hashValue, final long expireTime) {
        Map<String, V> valMap = new HashMap<>();
        valMap.put(hashKey, hashValue);
        hPut(key, valMap, expireTime);
    }

    /**
     * hash结构-put操作
     *
     * @param key      key
     * @param valueMap valueMap
     */
    public <V> void hPut(final String key, final Map<String, V> valueMap) {
        hPut(key, valueMap, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * hash结构-put操作
     *
     * @param key      key
     * @param valueMap valueMap
     */
    public <V> void hPut(final String key, final Map<String, V> valueMap, final long expireTime) {
        RMap<String, V> map = redissonClient.getMap(key);
        map.putAll(valueMap);
        //设置key的过期时间
        map.expire(Duration.ofSeconds(expireTime));
    }

    /**
     * hash结构-get操作
     *
     * @param key     key
     * @param hashKey hash key
     */
    public <V> V hGet(final String key, final String hashKey) {
        RMap<String, V> map = redissonClient.getMap(key);
        return map.get(hashKey);
    }

    /**
     * hash结构-读取指定key和hashKey的内容
     *
     * @param key key
     */
    public <V> Map<String, V> hGet(final String key, final Set<String> hashKeys) {
        RMap<String, V> map = redissonClient.getMap(key);
        return map.getAll(hashKeys);
    }

    /**
     * hash结构-一次性读取指定key的内容
     *
     * @param key key
     */
    public <V> Map<String, V> hGet(final String key) {
        RMap<String, V> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    /**
     * hash结构-delete操作
     *
     * @param key key
     * @return 存储内容
     */
    public boolean hDel(final String key) {
        return redissonClient.getMap(key).delete();
    }

    /**
     * hash结构-删除指定hashKey
     *
     * @param key  key
     * @param hKey hash key
     * @return 存储内容
     */
    public <V> V hRemove(final String key, final String hKey) {
        RMap<String, V> rMap = redissonClient.getMap(key);
        return rMap.remove(hKey);
    }

    /**
     * 按前缀删除
     *
     * @param pattern key前缀
     */
    public void delByPattern(final String pattern) {
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
     * list结构-尾部添加
     *
     * @param key      key
     * @param dataList List数据
     * @return boolean
     */
    public <V> boolean lPush(final String key, final List<V> dataList) {
        RList<V> rList = redissonClient.getList(key);
        boolean b = rList.addAll(dataList);
        rList.expire(DEFAULT_EXPIRE_DURATION);
        return b;
    }

    /**
     * list结构-尾部添加
     *
     * @param key    key
     * @param object 单条数据
     * @return 缓存的对象
     */
    public <V> boolean lPush(final String key, final V object) {
        return lPush(key, Collections.singletonList(object));
    }

    /**
     * list结构-获取所有元素
     *
     * @param key key
     * @return 缓存键值对应的数据
     */
    public <V> List<V> lGet(final String key) {
        RList<V> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * list结构-移除指定元素
     *
     * @param key    key
     * @param object 移除元素
     * @return
     */
    public <V> boolean lRemove(final String key, V object) {
        RList<V> rList = redissonClient.getList(key);
        return rList.remove(object);
    }

    /**
     * list结构-获取分页数据
     *
     * @param key        key
     * @param beginIndex 起始索引
     * @param endIndex   结束索引
     * @param <V>
     * @return 数据
     */
    public <V> List<V> lRange(final String key, final int beginIndex, final int endIndex) {
        RList<V> rList = redissonClient.getList(key);
        return rList.range(beginIndex, endIndex);
    }

    /**
     * list结构-列表数据大小
     *
     * @param key
     * @return
     */
    public int lLen(final String key) {
        return redissonClient.getList(key).size();
    }

    /**
     * set结构-添加数据
     *
     * @param key    key
     * @param object 数据
     * @param <V>
     * @return
     */
    public <V> boolean sPut(final String key, final V object) {
        return sPut(key, Collections.singleton(object));
    }

    /**
     * set结构-添加数据
     *
     * @param key     key
     * @param dataSet 数据
     * @param <V>
     * @return
     */
    public <V> boolean sPut(final String key, final Set<V> dataSet) {
        RSet<V> rSet = redissonClient.getSet(key);
        boolean b = rSet.addAll(dataSet);
        rSet.expire(DEFAULT_EXPIRE_DURATION);
        return b;

    }

    /**
     * set结构-获取数据
     *
     * @param key key
     * @return 数据
     */
    public <V> Set<V> sGet(final String key) {
        RSet<V> rSet = redissonClient.getSet(key);
        return rSet.readAll();
    }

    /**
     * set结构-移除元素
     *
     * @param key   key
     * @param value 移除元素
     * @return
     */
    public <V> boolean sRemove(final String key, final V value) {
        return redissonClient.getSet(key).remove(value);
    }

    /**
     * set结构-数据大小
     *
     * @param key key
     * @return 数据size
     */
    public int sLen(final String key) {
        return redissonClient.getSet(key).size();
    }

    /**
     * scoredSet-按score进行排序
     *
     * @param key
     * @param value
     * @param score
     * @param <V>
     * @return
     */
    public <V> boolean zAdd(final String key, final V value, final Double score) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.expire(DEFAULT_EXPIRE_DURATION);
        return sortedSet.add(score, value);
    }

    /**
     * scoredSet-按score进行排序，批量添加
     *
     * @param key
     * @param objects
     * @param <V>
     * @return
     */
    public <V> int zAdd(final String key, Map<V, Double> objects) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.expire(DEFAULT_EXPIRE_DURATION);
        return sortedSet.addAll(objects);
    }

    /**
     * scoredSet-指定元素添加分数
     *
     * @param key
     * @param value
     * @param number 增加的分数值
     * @param <V>
     * @return
     */
    public <V> Double zAddScore(final String key, V value, Number number) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.addScore(value, number);
    }

    /**
     * scoredSet-获取指定范围数据（从大到小排序）
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @param <V>
     * @return
     */
    public <V> Collection<V> zRange(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.valueRangeReversed(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据（从小到大排序）
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @param <V>
     * @return
     */
    public <V> Collection<V> zRangeLE(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.valueRange(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据和分数（从大到小排序）
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @param <V>
     * @return
     */
    public <V> Collection<ScoredEntry<V>> zEntryRange(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.entryRangeReversed(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据和分数（从小到大排序）
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @param <V>
     * @return
     */
    public <V> Collection<ScoredEntry<V>> zEntryRangeLE(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.entryRange(startIndex, endIndex);
    }

    /**
     * scoredSet-元素个数
     *
     * @param key
     * @param <V>
     * @return
     */
    public <V> int zLen(final String key) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.size();
    }

    /**
     * scoredSet-移除一个元素
     *
     * @param key
     * @param value
     * @param <V>
     * @return
     */
    public <V> boolean zRem(final String key, final V value) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.remove(value);
    }

    /**
     * scoredSet-移除多个元素
     *
     * @param key
     * @param values
     * @param <V>
     * @return
     */
    public <V> boolean zRem(final String key, final Collection<V> values) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.removeAll(values);
    }


}
