package com.zerosx.common.loadbalancer.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "feign.httpclient")
public class RestTemplateProperties {

    /**
     * 读取超时时间 ms
     */
    private int readTimeout = 35000;

    /**
     * 连接池获取连接的超时时间 ms
     */
    private int connectionRequestTimeout = 200;

}
