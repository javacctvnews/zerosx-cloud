package com.zerosx.sms.core.config;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * BaseSupplierConfig
 * <p> sms基础配置
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:29
 **/
@Data
@SuperBuilder
public class BaseSupplierConfig {

    /**
     * 运营商ID，默认配置 "000000"
     */
    private String operatorId = "000000";

    /**
     * Access Key
     */
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signature;

}
