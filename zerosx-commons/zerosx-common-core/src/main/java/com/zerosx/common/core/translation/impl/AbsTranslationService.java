package com.zerosx.common.core.translation.impl;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.feign.AsyncFeignService;
import com.zerosx.common.core.translation.ITranslationService;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.common.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AbsTranslationService
 * @Description
 * @Author javacctvnews
 * @Date 2023/5/26 11:05
 * @Version 1.0
 */
@Slf4j
public abstract class AbsTranslationService<T> implements ITranslationService<T> {

    protected static final String EMPTY = StringUtils.EMPTY;

    /**
     * 缓存，提高性能
     */
    protected AsyncCache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)//过期时间
            .maximumSize(1000)//最大条数1000
            .buildAsync();//定义cache

    /**
     * 获取Caffeine缓存
     *
     * @param key
     * @return
     */
    @SneakyThrows
    protected String getCaffeineCache(String key) {
        CompletableFuture<String> completableFuture = cache.getIfPresent(key);
        if (completableFuture == null) {
            //log.debug("【获取】Caffeine缓存, {} = 缓存为空", key);
            return EMPTY;
        }
        String cacheValue = completableFuture.get();
        //log.debug("【获取】Caffeine缓存 {} = {}", key, cacheValue);
        return cacheValue;
    }

    protected void putCache(String objectKey, String objectValue) {
        cache.put(objectKey, CompletableFuture.completedFuture(objectValue));
        //log.debug("【添加】Caffeine缓存, {} = {}", objectKey, objectValue);
    }

    protected RedissonOpService getRedissonOpService() {
        return SpringUtils.getBean(RedissonOpService.class);
    }

    protected AsyncFeignService getAsyncFeignService() {
        return SpringUtils.getBean(AsyncFeignService.class);
    }

    /**
     * 从Redisson缓存获取
     *
     * @param key
     * @return
     */
    protected abstract String getRedissonCache(String key);

    /**
     * 从api接口获取
     *
     * @param key
     * @return
     * @throws Exception
     */
    protected abstract ResultVO<?> getFeignService(String key) throws Exception;

    /**
     * 翻译 模板方法
     *
     * @param key
     * @return
     */
    protected String translation(Object key) {
        if (key == null || ObjectUtils.isEmpty(key)) {
            return EMPTY;
        }
        String objectName = String.valueOf(key);
        String cacheName = getCaffeineCache(objectName);
        if (StringUtils.isNotBlank(cacheName)) {
            return cacheName;
        }
        //先从Redis获取
        String redissonCache = getRedissonCache(objectName);
        if (redissonCache != null){
            putCache(objectName, redissonCache);
            return redissonCache;
        }
        ResultVO<?> res;
        try {
            res = getFeignService(objectName);
            Object data = res.getData();
            if (Objects.isNull(data)) {
                return EMPTY;
            }
            String endRes;
            if (data instanceof String) {
                endRes = (String) data;
            } else {
                endRes = JacksonUtil.toJSONString(data);
            }
            if (StringUtils.isNotBlank(endRes)) {
                putCache(objectName, endRes);
            }
            return endRes;
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
        return EMPTY;
    }

}
