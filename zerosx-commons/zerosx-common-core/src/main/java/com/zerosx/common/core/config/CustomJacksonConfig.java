package com.zerosx.common.core.config;

import com.fasterxml.jackson.databind.*;
import com.zerosx.common.base.utils.JacksonUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class CustomJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return customizer -> {
            ObjectMapper instance = JacksonUtil.getInstance();
            customizer.configure(instance);
        };
    }

}
