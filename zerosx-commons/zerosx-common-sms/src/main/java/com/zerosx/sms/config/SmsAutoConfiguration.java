package com.zerosx.sms.config;

import com.zerosx.sms.properties.CustomSmsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({CustomSmsProperties.class})
public class SmsAutoConfiguration {

}
