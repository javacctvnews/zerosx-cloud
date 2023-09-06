package com.zerosx.common.oss.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OssProperties {

    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String accessKeySecret;
    /**
     * 访问端点
     */
    private String endpoint;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 区域
     */
    //private String region;
    /**
     * path-style
     */
    //private Boolean pathStyleAccessEnabled = true;
    /**
     * viewUrl的有效期
     */
    private Integer expireDate = 31;

}
