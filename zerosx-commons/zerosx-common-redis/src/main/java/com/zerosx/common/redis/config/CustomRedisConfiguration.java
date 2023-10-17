package com.zerosx.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerosx.common.redis.handler.KeyPrefixHandler;
import com.zerosx.common.redis.properties.CustomRedissonProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.redisson.spring.starter.RedissonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * redis 配置类
 */
@AutoConfiguration
@EnableConfigurationProperties({RedissonProperties.class, RedisProperties.class, CustomRedissonProperties.class})
@EnableCaching
public class CustomRedisConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;
    @Autowired
    private CustomRedissonProperties customRedissonProperties;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * redisson的jdk序列化
     *
     * @return
     */
    @Bean
    public SerializationCodec serializationCodec() {
        return new SerializationCodec();
    }

    /**
     * 集群、哨兵、单节点 添加key前缀
     *
     * @return
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return config -> {
            //Redis数据编解码器
            config.setCodec(new JsonJacksonCodec(objectMapper));
            if (StringUtils.isBlank(customRedissonProperties.getKeyPrefix())) {
                return;
            }
            //设置key前缀
            Config yamlConfig;
            try {
                yamlConfig = Config.fromYAML(this.redissonProperties.getConfig());
            } catch (IOException var15) {
                try {
                    yamlConfig = Config.fromJSON(this.redissonProperties.getConfig());
                } catch (IOException var14) {
                    var14.addSuppressed(var15);
                    throw new IllegalArgumentException("Can't parse config", var14);
                }
            }
            KeyPrefixHandler keyPrefixHandler = new KeyPrefixHandler(customRedissonProperties.getKeyPrefix());
            if (yamlConfig.isClusterConfig()) {
                config.useClusterServers().setNameMapper(keyPrefixHandler);

            } else if (yamlConfig.isSentinelConfig()) {
                config.useSentinelServers().setNameMapper(keyPrefixHandler);
            } else {
                config.useSingleServer().setNameMapper(keyPrefixHandler);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonOpService redissonOpService(RedissonClient redissonClient) {
        return new RedissonOpService(redissonClient);
    }
}
