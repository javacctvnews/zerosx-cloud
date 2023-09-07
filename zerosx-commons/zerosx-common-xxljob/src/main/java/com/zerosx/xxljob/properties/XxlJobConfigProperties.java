package com.zerosx.xxljob.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties(prefix = "xxl.job")
@RefreshScope
public class XxlJobConfigProperties {

    //访问token，必须配置
    private String accessToken;

    //xxl-job-admin访问全地址
    private AdminConfig admin;

    //executor的配置
    private ExecutorConfig executor;


    @Getter@Setter
    public static class AdminConfig {
        private String addresses;
    }

    @Getter@Setter
    public static class ExecutorConfig {
        private String appname;
        private String address;
        private String ip;
        private Integer port;
        private String logpath;
        private Integer logretentiondays;
    }

}
