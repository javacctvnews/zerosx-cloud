package com.zerosx.resource.config;

import com.zerosx.encrypt2.core.properties.EncryptProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EncryptConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-15 09:57
 **/
@Configuration
public class EncryptConfig {

    @Bean
    @ConfigurationProperties(prefix = "zerosx.encrypt")
    public EncryptProperties encryptProperties(){
        return new EncryptProperties();
    }


}
