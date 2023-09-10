package com.zerosx.common.oss.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * BaseOssConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 17:03
 **/
@Getter
@Setter
public class BaseOssConfig implements IOssConfig {

    //服务商ID
    private Long ossSupplierId;

    /**
     * 类型，OssTypeEnum
     */
    private String supplierType;

    /**
     * 用户名
     */
    private String accessKeyId;
    /**
     * 密码
     */
    private String accessKeySecret;

    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * 区域
     */
    private String regionId;

}
