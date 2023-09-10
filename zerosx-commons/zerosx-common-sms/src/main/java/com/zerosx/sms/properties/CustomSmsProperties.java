package com.zerosx.sms.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * CustomSmsProperties
 * <p> 可通过yaml配置全局sms
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
     * 配置类全限定名
     */
    private String smsConfigClass;
    /**
     * 系统全局sms配置
     */
    private DefaultSmsProperties defaultSms;
    /**
     * 短信验证业务模板
     */
    private List<SmsBusinessProperties> smsBusinesses;


}
