package com.zerosx.resource.task;

import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.resource.core.IDGen;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * PostConstructTask
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-23 22:49
 **/
@Slf4j
@Component
public class PostConstructTask {

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private IDGen idGen;

    @PostConstruct
    public void delDictData() {
        redissonOpService.delByPattern(ZCache.DICT_DATA.key("*"));
    }

    @PostConstruct
    public void initLeaf() {
        boolean init = idGen.init();
        log.debug("初始化【IDGen】成功：{}~~~~~~~~~", init);
    }

}
