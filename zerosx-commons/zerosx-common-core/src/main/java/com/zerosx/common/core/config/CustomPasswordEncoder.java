package com.zerosx.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码
 */
@AutoConfiguration
public class CustomPasswordEncoder {

    private static final String BCRYPT = "bcrypt";

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return getPasswordEncoder(BCRYPT);
    }

    /**
     * 支持多种密码
     *
     * @param pwdType
     * @return
     */
    public PasswordEncoder getPasswordEncoder(String pwdType) {
        Map<String, PasswordEncoder> passwordEncoderMap = new HashMap<>();
        passwordEncoderMap.put(BCRYPT, new BCryptPasswordEncoder());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(pwdType, passwordEncoderMap);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(passwordEncoderMap.get(pwdType));
        return delegatingPasswordEncoder;
    }

}
