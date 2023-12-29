package com.zerosx.resource.task;

import com.zerosx.common.redis.templete.RedissonOpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ResourceAsyncTask
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/22 17:38
 * @Version 1.0
 */
@Component
@Slf4j
public class ResourceAsyncTask {

    @Autowired
    private RedissonOpService redissonOpService;

    /**
     * 延时删除keys
     *
     * @param keys
     */
    //@Async
    public void asyncRedisDelOptions(String... keys) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        long t1 = System.currentTimeMillis();
        redissonOpService.del(keys);
        log.debug("延时删除【{}】的缓存，耗时{}ms", keys, System.currentTimeMillis() - t1);
    }

}
