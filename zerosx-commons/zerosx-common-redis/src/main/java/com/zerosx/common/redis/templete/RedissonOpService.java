package com.zerosx.common.redis.templete;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.client.protocol.ScoredEntry;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redisson客户端的操作工具，推荐使用
 */
@Slf4j
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
     */
    private final RedissonClient redissonClient;

    public RedissonOpService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private <V> RBucket<V> getBucket(final String key, Codec codec) {
        if (codec == null) {
            return redissonClient.getBucket(key);
        }
        return redissonClient.getBucket(key, codec);
    }

    /**
     * 添加到缓存
     */
    public <V> void set(final String key, final V value) {
        set(key, value, DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 添加到缓存,指定Codec
     */
    public <V> void set(final String key, final V value, Codec codec) {
        set(key, value, DEFAULT_EXPIRE_SECONDS, codec);
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
        set(key, value, expireTime, null);
    }

    /**
     * 添加到缓存,指定Codec
     *
     * @param key
     * @param value
     * @param expireTime
     * @param codec
     * @param <V>
     */
    public <V> void set(final String key, final V value, final long expireTime, Codec codec) {
        getBucket(key, codec).set(value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 不存在时设值
     * Params:
     * value – value to set
     * duration – expiration duration
     * Returns: true if successful, or false if element was already set
     */
    public <V> boolean setIfAbsent(final String key, final V value, final long expireTime) {
        return getBucket(key, null).setIfAbsent(value, Duration.ofSeconds(expireTime));
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
        RBucket<V> bucket = getBucket(key, null);
        return bucket.get();
    }

    public <V> V get(final String key, Codec codec) {
        RBucket<V> rBucket = getBucket(key, codec);
        return rBucket.get();
    }

    /**
     * 获取key的过期时间，单位：秒
     * time in milliseconds -2 if the key does not exist. -1 if the key exists but has no associated expire.
     *
     * @param key key
     * @return 过期时间，单位：秒，-2表示key不存在(已过期)，-1表示key存在但是未关联过期时间
     */
    public long ttl(final String key) {
        //毫秒
        long timeToLive = getBucket(key, null).remainTimeToLive();
        if (timeToLive > 0) {
            return timeToLive / MILLI_SECONDS;
        }
        return timeToLive;
    }

    /**
     * 设置过期时间
     *
     * @param key        key
     * @param expireTime 过期时间，单位：秒
     * @return 设置结果
     */
    public boolean expire(final String key, final Integer expireTime) {
        return getBucket(key, null).expire(Duration.ofSeconds(expireTime));
    }

    public boolean expire(final String key, final Integer expireTime, Codec codec) {
        return getBucket(key, codec).expire(Duration.ofSeconds(expireTime));
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
    public boolean del(final String key) {
        return getBucket(key, null).delete();
    }

    public boolean del(final String key, Codec codec) {
        return getBucket(key, codec).delete();
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

    public boolean del(final List<String> keys) {
        for (String key : keys) {
            if (StringUtils.isNotBlank(key)) {
                del(key);
            }
        }
        return true;
    }

    private <V> RMap<String, V> getRMap(final String key, Codec codec) {
        if (codec == null) {
            return redissonClient.getMap(key);
        }
        return redissonClient.getMap(key, codec);
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
        RMap<String, V> map = getRMap(key, null);
        map.putAll(valueMap);
        //设置key的过期时间
        map.expire(Duration.ofSeconds(expireTime));
    }

    public <V> void hPut(final String key, final String hashKey, final V value, final long expireTime, Codec codec) {
        RMap<String, V> map = getRMap(key, codec);
        map.put(hashKey, value);
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
        RMap<String, V> map = getRMap(key, null);
        return map.get(hashKey);
    }

    public <V> V hGet(final String key, final String hashKey, Codec codec) {
        RMap<String, V> map = getRMap(key, codec);
        V v = map.get(hashKey);
        return v;
    }

    /**
     * hash结构-读取指定key和hashKey的内容
     *
     * @param key key
     */
    public <V> Map<String, V> hGet(final String key, final Set<String> hashKeys) {
        RMap<String, V> map = getRMap(key, null);
        return map.getAll(hashKeys);
    }

    /**
     * hash结构-一次性读取指定key的内容
     *
     * @param key key
     */
    public <V> Map<String, V> hGet(final String key) {
        RMap<String, V> map = getRMap(key, null);
        return map.readAllMap();
    }

    /**
     * hash结构-delete操作
     *
     * @param key key
     * @return 存储内容
     */
    public boolean hDel(final String key) {
        return getRMap(key, null).delete();
    }

    public boolean hDel(final String key, Codec codec) {
        return getRMap(key, codec).delete();
    }

    /**
     * hash结构-删除指定hashKey
     *
     * @param key  key
     * @param hKey hash key
     * @return 存储内容
     */
    public <V> V hRemove(final String key, final String hKey) {
        return hRemove(key, hKey, null);
    }

    public <V> V hRemove(final String key, final String hKey, Codec codec) {
        RMap<String, V> rMap = getRMap(key, codec);
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

    /*public void delByPattern(final String pattern, Codec codec) {
        redissonClient.getKeys().deleteByPattern(pattern, codec);
    }*/

    /**
     * 是否存在key
     *
     * @param key 键
     */
    public Boolean existKey(final String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.countExists(key) > 0;
    }

    private <V> RList<V> getRList(final String key, Codec codec) {
        return redissonClient.getList(key, codec);
    }

    private <V> RList<V> getRList(final String key) {
        return redissonClient.getList(key);
    }

    /**
     * list结构-尾部添加
     *
     * @param key      key
     * @param dataList List数据
     * @return boolean
     */
    public <V> boolean lPush(final String key, final List<V> dataList) {
        RList<V> rList = getRList(key);
        boolean b = rList.addAll(dataList);
        rList.expire(DEFAULT_EXPIRE_DURATION);
        return b;
    }

    public <V> boolean lPush(final String key, final List<V> dataList, Codec codec) {
        RList<V> rList = getRList(key, codec);
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

    public <V> boolean lPush(final String key, final V object, Codec codec) {
        return lPush(key, Collections.singletonList(object), codec);
    }

    /**
     * list结构-获取所有元素
     *
     * @param key key
     * @return 缓存键值对应的数据
     */
    public <V> List<V> lGet(final String key) {
        RList<V> rList = getRList(key);
        return rList.readAll();
    }

    public <V> List<V> lGet(final String key, Codec codec) {
        RList<V> rList = getRList(key, codec);
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
        RList<V> rList = getRList(key);
        return rList.remove(object);
    }

    public <V> boolean lRem(final String key, final V object, int count) {
        RList<V> rList = getRList(key);
        return rList.remove(object, count);
    }

    public <V> boolean lRem(final String key, final V object, int count, Codec codec) {
        RList<V> rList = getRList(key, codec);
        return rList.remove(object, count);
    }

    /**
     * list结构-获取分页数据
     *
     * @param key        key
     * @param beginIndex 起始索引
     * @param endIndex   结束索引
     * @return 数据
     */
    public <V> List<V> lRange(final String key, final int beginIndex, final int endIndex) {
        RList<V> rList = getRList(key);
        return rList.range(beginIndex, endIndex);
    }

    /**
     * list结构-获取分页数据
     *
     * @param key        key
     * @param beginIndex 起始索引
     * @param endIndex   结束索引
     * @param codec      codec
     * @return 数据
     */
    public <V> List<V> lRange(final String key, final int beginIndex, final int endIndex, Codec codec) {
        RList<V> rList = getRList(key, codec);
        return rList.range(beginIndex, endIndex);
    }

    /**
     * list结构-列表数据大小
     *
     * @param key key
     * @return
     */
    public int lLen(final String key) {
        return getRList(key).size();
    }

    /**
     * list结构-列表数据大小
     *
     * @param key   key
     * @param codec 序列化实例
     * @return
     */
    public int lLen(final String key, Codec codec) {
        return getRList(key, codec).size();
    }

    public <V> RSet<V> getRSet(final String key) {
        return redissonClient.getSet(key);
    }

    public <V> RSet<V> getRSet(final String key, Codec codec) {
        return redissonClient.getSet(key, codec);
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
        RSet<V> rSet = getRSet(key);
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
        RSet<V> rSet = getRSet(key);
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
        return getRSet(key).remove(value);
    }

    /**
     * set结构-数据大小
     *
     * @param key key
     * @return 数据size
     */
    public int sLen(final String key) {
        return getRSet(key).size();
    }

    /**
     * sorted set数据结构对象
     *
     * @param key   key
     * @param codec 编解码器
     * @return RScoredSortedSet<V>
     */
    private <V> RScoredSortedSet<V> getScoredSortedSet(final String key, Codec codec) {
        if (codec == null) {
            return redissonClient.getScoredSortedSet(key);
        }
        return redissonClient.getScoredSortedSet(key, codec);
    }

    /**
     * sorted set数据结构对象
     *
     * @param key key
     * @return RScoredSortedSet<V>
     */
    private <V> RScoredSortedSet<V> getScoredSortedSet(final String key) {
        return getScoredSortedSet(key, null);
    }

    /**
     * scoredSet-按score进行排序
     *
     * @param key   key
     * @param value value
     * @param score score
     */
    public <V> boolean zAdd(final String key, final V value, final Double score) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        sortedSet.expire(DEFAULT_EXPIRE_DURATION);
        return sortedSet.add(score, value);
    }

    /**
     * scoredSet-按score进行排序
     *
     * @param key   key
     * @param value value
     * @param score score
     * @param codec 编解码器
     */
    public <V> boolean zAdd(final String key, final V value, final Double score, Codec codec) {
        return zAdd(key, value, score, DEFAULT_EXPIRE_SECONDS, codec);
    }

    /**
     * scoredSet-按score进行排序-可设置过期时间
     *
     * @param key        key
     * @param value      value
     * @param score      score
     * @param expireTime expireTime
     * @param codec      编解码器
     */
    public <V> boolean zAdd(final String key, final V value, final Double score, final long expireTime, Codec codec) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key, codec);
        sortedSet.expire(Duration.ofSeconds(expireTime));
        return sortedSet.add(score, value);
    }

    /**
     * scoredSet-按score进行排序，批量添加
     *
     * @param key      可以
     * @param valueMap 新增数据
     * @return 新增的元素个数，不包括已经存在的
     */
    public <V> int zAdd(final String key, Map<V, Double> valueMap) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        sortedSet.expire(DEFAULT_EXPIRE_DURATION);
        return sortedSet.addAll(valueMap);
    }

    /**
     * scoredSet-指定元素添加分数
     *
     * @param key    key
     * @param value  value
     * @param number 增加的分数值
     * @return 更新后的score
     */
    public <V> Double zAddScore(final String key, V value, Number number) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.addScore(value, number);
    }

    /**
     * scoredSet-获取指定范围数据（从大到小排序）
     *
     * @param key        key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 数据
     */
    public <V> Collection<V> zRange(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.valueRangeReversed(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据（从大到小排序）
     *
     * @param key        key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @param codec      编解码器
     * @return 数据
     */
    public <V> Collection<V> zRange(final String key, int startIndex, int endIndex, Codec codec) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key, codec);
        return sortedSet.valueRangeReversed(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据（从小到大排序）
     *
     * @param key        key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 符合范围数据和分数
     */
    public <V> Collection<V> zRangeLE(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.valueRange(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据和分数（从大到小排序）
     *
     * @param key        key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 符合范围数据和分数
     */
    public <V> Collection<ScoredEntry<V>> zEntryRange(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.entryRangeReversed(startIndex, endIndex);
    }

    /**
     * scoredSet-获取指定范围数据和分数（从小到大排序）
     *
     * @param key        key
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 符合范围数据和分数
     */
    public <V> Collection<ScoredEntry<V>> zEntryRangeLE(final String key, int startIndex, int endIndex) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.entryRange(startIndex, endIndex);
    }

    /**
     * scoredSet-获取元素个数
     *
     * @param key key
     * @return 元素个数
     */
    public int zLen(final String key) {
        return zLen(key, null);
    }

    /**
     * scoredSet-获取元素个数
     *
     * @param key key
     * @return 元素个数
     */
    public int zLen(final String key, Codec codec) {
        return getScoredSortedSet(key, codec).size();
    }

    /**
     * scoredSet-移除一个元素
     *
     * @param key   key
     * @param value 移除的元素
     */
    public <V> boolean zRem(final String key, final V value) {
        return zRem(key, value, null);
    }

    /**
     * scoredSet-移除一个元素
     *
     * @param key   key
     * @param value 移除的元素
     * @param codec Codec编解码器
     */
    public <V> boolean zRem(final String key, final V value, Codec codec) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key, codec);
        return sortedSet.remove(value);
    }

    /**
     * scoredSet-移除多个元素
     *
     * @param key    key
     * @param values 需要移除的元素集合
     */
    public <V> boolean zRem(final String key, final Collection<V> values) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.removeAll(values);
    }

    /**
     * 删除小于某个分数的记录
     */
    public <V> int zRemByScore(final String key, final Double endScore, Codec codec) {
        return zRemByScore(key, (double) 0, endScore, codec);
    }

    public <V> int zRemByScore(final String key, final Double startScore, final Double endScore, Codec codec) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key, codec);
        return sortedSet.removeRangeByScore(startScore, true, endScore, true);
    }

    public <V> Collection<V> zGetByScore(final String key, final Double endScore, Codec codec) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key, codec);
        return sortedSet.valueRangeReversed(0, true, endScore, true);
    }

    public <V> Collection<V> zGetByScore(final String key, final Double endScore) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.valueRangeReversed(0, true, endScore, true);
    }

    public <V> Collection<V> zGetByScore(final String key, final Double endScore, int offset, int count) {
        RScoredSortedSet<V> sortedSet = getScoredSortedSet(key);
        return sortedSet.valueRangeReversed(0, true, endScore, true, offset, count);
    }

}
