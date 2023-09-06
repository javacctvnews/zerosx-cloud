package com.zerosx.common.oss;

import com.aliyun.oss.OSS;
import com.zerosx.common.oss.config.OssConfiguration;
import com.zerosx.common.oss.properties.FileServerProperties;
import com.zerosx.common.oss.templete.S3FileOpService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({S3FileOpService.class})
@EnableConfigurationProperties(FileServerProperties.class)
public class CustomOSSConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public S3FileOpService s3FileOpService(OSS ossClient, FileServerProperties fileServerProperties){
        return new S3FileOpService(ossClient,fileServerProperties);
    }

}
