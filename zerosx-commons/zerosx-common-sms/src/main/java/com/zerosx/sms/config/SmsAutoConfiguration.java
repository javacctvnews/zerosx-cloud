package com.zerosx.sms.config;

import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.core.config.JuheConfig;
import com.zerosx.sms.core.provider.AlibabaProviderFactory;
import com.zerosx.sms.core.provider.JuheProviderFactory;
import com.zerosx.sms.properties.CustomSmsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({CustomSmsProperties.class})
public class SmsAutoConfiguration {

    /**
     * 阿里云短信配置(默认配置)
     */
    @Bean("alibabaConfig")
    @ConfigurationProperties(prefix = "zerosx.sms.alibaba")
    protected AlibabaConfig alibabaConfig() {
        return AlibabaProviderFactory.instance().getConfig();
    }

    /**
     * 阿里云短信配置
     */
    @Bean("juheConfig")
    @ConfigurationProperties(prefix = "zerosx.sms.juhe")
    protected JuheConfig juheConfig() {
        return JuheProviderFactory.instance().getConfig();
    }


}
