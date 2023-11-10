package com.zerosx.auth.config.auth;

import com.zerosx.auth.granter.pwd.PasswordAuthenticationProvider;
import com.zerosx.auth.service.userdetails.CustomUserDetailsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AuthenticationManagerConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-20 16:23
 **/
@Configuration
public class AuthenticationManagerConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        PasswordAuthenticationProvider provider = new PasswordAuthenticationProvider();
        provider.setCustomUserDetailsServiceFactory(customUserDetailsServiceFactory);
        provider.setPasswordEncoder(passwordEncoder);
        authenticationManagerBuilder.authenticationProvider(provider);
        return authenticationConfiguration.getAuthenticationManager();
    }




}
