package com.zerosx.common.oss.config;


import com.zerosx.common.oss.properties.DefaultOssProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DefaultOssProperties.class)
public class OssConfiguration {


}
