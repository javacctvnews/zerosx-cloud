package com.zerosx.common.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.redis"
)
@Setter@Getter
public class CustomRedissonProperties {

    /**
     * redisson操作下设置key全局前缀
     */
    private String keyPrefix;

}
