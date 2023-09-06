package com.zerosx.common.oss.config;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zerosx.common.oss.properties.FileServerProperties;
import com.zerosx.common.oss.properties.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OSS ossClient(FileServerProperties fileServerProperties){
        OssProperties ossProperties = fileServerProperties.getS3();
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getAccessKeySecret(), conf);
    }

}
