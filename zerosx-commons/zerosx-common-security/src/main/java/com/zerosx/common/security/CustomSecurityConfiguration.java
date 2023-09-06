package com.zerosx.common.security;

import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import com.zerosx.common.security.store.CustomRedisTokenStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

@AutoConfiguration
@EnableConfigurationProperties({CustomSecurityProperties.class})
public class CustomSecurityConfiguration {

    @Bean
    public TokenStore tokenStore(RedissonOpService redissonOpService, RedisConnectionFactory connectionFactory, CustomSecurityProperties customSecurityProperties) {
        return new CustomRedisTokenStore(redissonOpService, connectionFactory, customSecurityProperties);
    }

}
