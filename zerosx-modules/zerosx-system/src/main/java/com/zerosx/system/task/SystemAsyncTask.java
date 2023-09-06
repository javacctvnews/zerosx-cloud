package com.zerosx.system.task;

import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.system.service.ISysDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName SaasAsyncTask
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/22 17:38
 * @Version 1.0
 */
@Component
@Slf4j
public class SystemAsyncTask {

    private static final Integer DELAY_TIME = 15 * 1000;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 延时删除keys
     *
     * @param keys
     */
    @Async
    public void asyncRedisDelOptions(String... keys) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long t1 = System.currentTimeMillis();
        redissonOpService.del(keys);
        log.debug("延时删除【{}】的缓存，耗时{}ms", keys, System.currentTimeMillis() - t1);
    }

    @Async
    public void initCacheDictData() {
        try {
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long t1 = System.currentTimeMillis();
        sysDictDataService.initCacheDictData(null);
        log.debug("init dictData cost {}ms", (System.currentTimeMillis() - t1));
    }
}
