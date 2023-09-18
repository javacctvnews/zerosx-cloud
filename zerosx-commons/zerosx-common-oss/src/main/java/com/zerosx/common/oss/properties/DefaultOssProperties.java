package com.zerosx.common.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统默认的oss配置
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "zerosx.oss")
public class DefaultOssProperties {

    /**
     * 文件前缀，区分不同系统，可空
     */
    private String filePrefix;

    /**
     * 配置类全限定名
     */
    private String ossConfigClz;

    /**
     * 可上传的文件大小的最大值,默认50M
     */
    private long defaultMaxSize = 50 * 1024 * 1024;

    /**
     * 默认的oss配置
     */
    private DefaultOssConfig ossConfig;

    @Getter
    @Setter
    public static class DefaultOssConfig {
        /**
         * OssTypeEnum
         */
        private String supplierType;
        /**
         * accessKey
         */
        private String accessKeyId;
        /**
         * accessKeySecret
         */
        private String accessKeySecret;
        /**
         * 存储桶名称
         */
        private String bucketName;
        /**
         * region
         */
        private String regionId;
        /**
         * endpoint
         */
        private String endpoint;
        /**
         * expireTime失效时间，不同oss服务商不一样
         */
        private Long expireTime = 28800L;

        /**
         * 七牛云服务商独有 域名
         */
        private String domainAddress;

    }

}
