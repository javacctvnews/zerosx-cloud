package com.zerosx.resource.task;

import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PostConstructTask
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-23 22:49
 **/
@Component
public class PostConstructTask {

    @Autowired
    private RedissonOpService redissonOpService;

    @PostConstruct
    public void delDictData() {
        redissonOpService.delByPattern(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, "*"));
    }


}
