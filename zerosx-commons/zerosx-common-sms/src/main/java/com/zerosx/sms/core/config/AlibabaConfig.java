package com.zerosx.sms.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * AlibabaConfig
 * <p>阿里巴巴短信服务商配置
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:30
 **/
@Getter@Setter
public class AlibabaConfig extends BaseSupplierConfig {

    /**
     * 地域信息默认为 cn-hangzhou
     */
    private String regionId = "";

}
