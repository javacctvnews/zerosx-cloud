package com.zerosx.auth.config.auth;

import com.zerosx.auth.granter.pwd.PasswordAuthenticationProvider;
import com.zerosx.auth.granter.sms.SmsAuthenticationSecurityConfig;
import com.zerosx.auth.service.userdetails.CustomUserDetailsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private SmsAuthenticationSecurityConfig smsAuthenticationSecurityConfig;
    @Autowired
    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置用户安全拦截策略
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and()
                //.logout()
                //.addLogoutHandler(oauthLogoutHandler)
                //.clearAuthentication(true)
                //.and()
                .apply(smsAuthenticationSecurityConfig)
                .and()
                .csrf().disable().headers().frameOptions().disable().cacheControl();
        http.formLogin()//可以从默认的login登录界面登录
                .successHandler(authenticationSuccessHandler);
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordAuthenticationProvider provider = new PasswordAuthenticationProvider();
        provider.setCustomUserDetailsServiceFactory(customUserDetailsServiceFactory);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);
    }
}
