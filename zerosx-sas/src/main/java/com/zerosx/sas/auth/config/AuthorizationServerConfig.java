package com.zerosx.sas.auth.config;

import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.sas.auth.grant.CustomPwdDaoAuthenticationProvider;
import com.zerosx.sas.auth.grant.captcha.CaptchaAuthenticationConverter;
import com.zerosx.sas.auth.grant.captcha.CaptchaAuthenticationProvider;
import com.zerosx.sas.auth.grant.pwd.PwdAuthenticationConverter;
import com.zerosx.sas.auth.grant.pwd.PwdAuthenticationProvider;
import com.zerosx.sas.auth.grant.sms.SmsAuthenticationConverter;
import com.zerosx.sas.auth.grant.sms.SmsAuthenticationProvider;
import com.zerosx.sas.auth.handler.CustomAuthenticationFailureHandler;
import com.zerosx.sas.auth.handler.CustomAuthenticationSuccessHandler;
import com.zerosx.sas.auth.userdetails.CustomUserDetailsServiceFactory;
import com.zerosx.sas.service.IOauthTokenRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * SecurityConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-20 23:16
 **/
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Autowired
    private RegisteredClientRepository registeredClientRepository;
    @Autowired
    private OAuth2AuthorizationService oAuth2AuthorizationService;
    @Autowired
    private OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private AuthorizationServerSettings authorizationServerSettings;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomPwdDaoAuthenticationProvider customPwdDaoAuthenticationProvider;
    @Autowired
    private IOauthTokenRecordService oauthTokenRecordService;
    @Autowired
    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    @Bean
    @Order()
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
        http
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().permitAll();
                })
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        http.apply(configurer)
                // 开启OpenID Connect 1.0协议相关端点
                .oidc(Customizer.withDefaults())
                .tokenGenerator(oAuth2TokenGenerator)
                .authorizationService(oAuth2AuthorizationService)
                .authorizationServerSettings(authorizationServerSettings)
                .registeredClientRepository(registeredClientRepository)
                .tokenEndpoint((endpoint) -> {
                    endpoint.accessTokenRequestConverter(accessTokenRequestConverter());
                    endpoint.authenticationProviders(authenticationProviders());
                    endpoint.errorResponseHandler(new CustomAuthenticationFailureHandler(oauthTokenRecordService));
                    endpoint.accessTokenResponseHandler(new CustomAuthenticationSuccessHandler(oauthTokenRecordService));
                }).clientAuthentication(client -> {
                    client.errorResponseHandler(new CustomAuthenticationFailureHandler(oauthTokenRecordService));
                });
        return http.formLogin(Customizer.withDefaults()).build();

    }


    /**
     * 请求转换器
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                //用户名密码+验证码 授权模式
                new CaptchaAuthenticationConverter(redissonOpService),
                // 用户名密码 授权模式
                new PwdAuthenticationConverter(),
                // 手机号码+验证码 授权模式
                new SmsAuthenticationConverter(redissonOpService)
        ));
    }

    /**
     * AuthenticationProvider
     *
     * @return Consumer<List < AuthenticationProvider>>
     */
    private Consumer<List<AuthenticationProvider>> authenticationProviders() {
        return provider -> {
            // 用户名密码 授权模式
            provider.add(new PwdAuthenticationProvider(oAuth2AuthorizationService, oAuth2TokenGenerator, authenticationManager));
            //用户名密码+验证码 授权模式
            provider.add(new CaptchaAuthenticationProvider(oAuth2AuthorizationService, oAuth2TokenGenerator, authenticationManager));
            // 手机号码+验证码 授权模式
            provider.add(new SmsAuthenticationProvider(oAuth2AuthorizationService, oAuth2TokenGenerator, authenticationManager, customUserDetailsServiceFactory));
            //DaoAuthenticationProvider扩展
            provider.add(customPwdDaoAuthenticationProvider);
        };
    }

}
