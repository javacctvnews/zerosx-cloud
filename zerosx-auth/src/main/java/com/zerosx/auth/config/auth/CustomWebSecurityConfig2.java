//package com.zerosx.auth.config.auth;
//
//import com.zerosx.auth.granter.sms.SmsAuthenticationSecurityConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//@Configuration
//public class CustomWebSecurityConfig2 {
//
//    @Autowired
//    private SmsAuthenticationSecurityConfig smsAuthenticationSecurityConfig;
//    @Autowired
//    private AuthenticationSuccessHandler authenticationSuccessHandler;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .headers((header) -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .authorizeHttpRequests(authorizeUrl -> authorizeUrl.anyRequest().permitAll())
//                .apply(smsAuthenticationSecurityConfig);
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin((formL) -> formL.successHandler(authenticationSuccessHandler));
//        return httpSecurity.build();
//    }
//}
