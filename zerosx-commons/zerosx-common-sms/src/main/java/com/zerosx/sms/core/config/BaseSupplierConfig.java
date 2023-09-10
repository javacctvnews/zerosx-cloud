package com.zerosx.sms.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * BaseSupplierConfig
 * <p> sms基础配置
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:29
 **/
@Setter@Getter
public abstract class BaseSupplierConfig implements ISupplierConfig{

    /**
     * 运营商ID，默认配置 "000000"
     */
    private String operatorId = "000000";
    /**
     * sms服务商编码
     */
    private String supplierType;

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
