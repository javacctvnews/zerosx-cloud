package com.zerosx.sms.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CustomSmsProperties
 * <p> 可通过yaml配置sms，需自行实现
 *
 * @author: javacctvnews
 * @create: 2023-09-01 16:37
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "zerosx.sms")
public class CustomSmsProperties {

    /**
     * yaml配置
     */
    private Boolean yamlConfig = false;
    /**
     * 默认的服务厂商配置
     */
    private String defaultSupplierType;


}
