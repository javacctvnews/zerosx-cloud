package com.zerosx.idempotent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * IdempotentProperties
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-19 16:52
 **/
@ConfigurationProperties(
        prefix = "zerosx.idempotent"
)
@Setter
@Getter
public class IdempotentProperties {

    /**
     * 锁key前缀
     */
    private String lockKeyPrefix = "idempotent";

    /**
     * Token 申请后过期时间
     * 单位默认秒 {@link TimeUnit#SECONDS}
     */
    private Long waitTime = 3L;

    /**
     * 锁最大释放时间
     * 单位默认秒 {@link TimeUnit#SECONDS}
     */
    private Long leaseTime = 300L;

}
