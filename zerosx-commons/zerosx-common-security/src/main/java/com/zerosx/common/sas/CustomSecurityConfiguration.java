package com.zerosx.common.sas;

import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.auth.CustomOAuth2AuthorizationService;
import com.zerosx.common.sas.properties.CustomSecurityProperties;
import org.redisson.codec.SerializationCodec;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({CustomSecurityProperties.class})
public class CustomSecurityConfiguration {

    @Bean
    public CustomOAuth2AuthorizationService customOAuth2AuthorizationService(RedissonOpService redissonOpService, SerializationCodec serializationCodec) {
        return new CustomOAuth2AuthorizationService(redissonOpService, serializationCodec);
    }

}
