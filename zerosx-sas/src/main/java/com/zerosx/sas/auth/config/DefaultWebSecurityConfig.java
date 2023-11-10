package com.zerosx.sas.auth.config;

import com.zerosx.sas.auth.grant.CustomDaoAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * DefaultWebSecurityConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-24 08:48
 **/
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultWebSecurityConfig {

    @Autowired
    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            //authorize.requestMatchers("/**").permitAll();

            authorize.anyRequest().permitAll();

            /*authorize.requestMatchers("/oauth2/**").permitAll()
                    .anyRequest().authenticated();*/
        });
        http.authenticationProvider(customDaoAuthenticationProvider);
        return http.build();
    }


}
