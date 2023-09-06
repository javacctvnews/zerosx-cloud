package com.zerosx.common.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author javacctvnews
 * @description  日志链路追踪配置
 * @date Created in 2021/1/24 22:04
 * @modify by
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "zerosx.trace")
@RefreshScope
public class TraceProperties {

    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = true;

}
