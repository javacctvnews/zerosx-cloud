package com.zerosx.sms.core.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * AlibabaConfig
 * <p>阿里巴巴短信服务商配置
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:30
 **/
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AlibabaConfig extends BaseSupplierConfig implements ISupplierConfig {

    /**
     * 地域信息默认为 cn-hangzhou
     */
    private String regionId = "";

    /**
     * endpoint
     */
    //private String endpoint = "";

}
