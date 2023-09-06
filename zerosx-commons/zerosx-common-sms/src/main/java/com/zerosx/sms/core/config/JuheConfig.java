package com.zerosx.sms.core.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.web.client.RestTemplate;

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
public class JuheConfig extends BaseSupplierConfig implements ISupplierConfig {

    private String key = "";

    /**
     * http://v.juhe.cn/sms/send
     */
    private String domainAddress = "";

}
