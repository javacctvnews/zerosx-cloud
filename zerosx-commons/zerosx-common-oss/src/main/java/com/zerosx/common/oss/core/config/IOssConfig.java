package com.zerosx.common.oss.core.config;

/**
 * oss配置
 */
public interface IOssConfig {

    String getSupplierType();

    String getAccessKeyId();

    String getBucketName();

    Long getOssSupplierId();

    void setOssSupplierId(Long ossSupplierId);

    //Long getExpireTime();

}
