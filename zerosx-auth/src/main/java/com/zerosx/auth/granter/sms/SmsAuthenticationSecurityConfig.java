package com.zerosx.auth.granter.sms;

import com.zerosx.auth.service.userdetails.CustomUserDetailsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;


@Component
public class SmsAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    @Override
    public void configure(HttpSecurity http) {
        SmsAuthenticationProvider provider = new SmsAuthenticationProvider();
        provider.setCustomUserDetailsServiceFactory(customUserDetailsServiceFactory);
        http.authenticationProvider(provider);
    }

}
