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
@Setter@Getter
public class JuheConfig extends BaseSupplierConfig {

    private String key = "";

    /**
     * http://v.juhe.cn/sms/send
     */
    private String domainAddress = "";

}
