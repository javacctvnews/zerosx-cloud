package com.zerosx.common.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "zerosx.file-server")
public class FileServerProperties {

    public static final String S3 = "s3";

    /**
     * 为以下2个值，指定不同的自动化配置
     * s3：aws s3协议的存储（七牛oss、阿里云oss、minio等）
     * fastdfs：本地部署的fastDFS
     * aliyun：阿里云oss
     */
    private String type = S3;

    /**
     * 文件前缀，区分不同系统，可空
     */
    private String filePrefix;

    /**
     * 可上传的文件大小的最大值,默认50M
     */
    private long defaultMaxSize = 50 * 1024 * 1024;

    OssProperties s3 = new OssProperties();

}
