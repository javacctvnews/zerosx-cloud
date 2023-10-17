package com.zerosx.common.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Set;

/**
 * @author javacctvnews
 * @description 日志链路追踪配置
 * @date Created in 2021/1/24 22:04
 * @modify by
 */
@Data
@ConfigurationProperties(prefix = "zerosx.log")
@RefreshScope
public class CustomLogProperties {

    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = true;

    /**
     * 使用内部feign接口保存操作日志
     */
    private Boolean saveByFeign = true;

    /**
     * 采用redis保存日志，再从redis保存到mysql
     */
    private Boolean saveByRedis = false;

    /**
     * 操作参数不序列化的字段
     */
    private Set<String> excludeFields;


}
