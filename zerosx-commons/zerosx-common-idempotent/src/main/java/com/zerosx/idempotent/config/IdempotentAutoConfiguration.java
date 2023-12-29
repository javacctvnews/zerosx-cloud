package com.zerosx.idempotent.config;

import com.zerosx.idempotent.aspect.IdempotentAspect;
import com.zerosx.idempotent.core.param.IdempotentParamHandler;
import com.zerosx.idempotent.core.spel.IdempotentSpELHandler;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * IdempotentAutoConfigurtion
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-18 17:04
 **/
@EnableConfigurationProperties({IdempotentProperties.class})
public class IdempotentAutoConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamHandler idempotentParamHandler(IdempotentProperties idempotentProperties, RedissonClient redissonClient) {
        return new IdempotentParamHandler(idempotentProperties, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELHandler idempotentSpelHandler(IdempotentProperties idempotentProperties, RedissonClient redissonClient) {
        return new IdempotentSpELHandler(idempotentProperties, redissonClient);
    }

}
