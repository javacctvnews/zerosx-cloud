package com.zerosx.gateway.config;


import com.zerosx.common.log.properties.CustomLogProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import com.zerosx.gateway.auth.*;
import com.zerosx.gateway.feign.AsyncSysUserService;
import com.zerosx.gateway.filter.CustomServerWebExchangeContextFilter;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.gateway.filter.TraceIDFilter;
import com.zerosx.gateway.handler.CustomAccessDeniedHandler;
import com.zerosx.gateway.handler.CustomAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;

import java.util.List;

/**
 * 资源服务器配置
 *
 * @author javacctvnews
 */
@Configuration
@Slf4j
public class ResourceServerConfig {

    @Autowired
    private CustomSecurityProperties customSecurityProperties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private CustomAuthorizationManager customAuthorizationManager;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private AsyncSysUserService asyncLoginUserService;
    @Autowired
    private CustomLogProperties customLogProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        //log
        http.addFilterAt(new TraceIDFilter(customLogProperties), SecurityWebFiltersOrder.FIRST);
        //Context
        http.addFilterAt(new CustomServerWebExchangeContextFilter(tokenStore, redissonOpService, asyncLoginUserService), SecurityWebFiltersOrder.REACTOR_CONTEXT);
        //认证处理器
        ReactiveAuthenticationManager customAuthenticationManager = new CustomAuthenticationManager(tokenStore);
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        //token转换器
        ServerBearerTokenAuthenticationConverter tokenAuthenticationConverter = new ServerBearerTokenAuthenticationConverter();
        tokenAuthenticationConverter.setAllowUriQueryParameter(true);
        //oauth2认证过滤器
        AuthenticationWebFilter oauth2Filter = new AuthenticationWebFilter(customAuthenticationManager);
        oauth2Filter.setServerAuthenticationConverter(tokenAuthenticationConverter);
        oauth2Filter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        oauth2Filter.setRequiresAuthenticationMatcher(new CustomServerWebExchangeMatcher(customSecurityProperties));
        http.addFilterAt(oauth2Filter, SecurityWebFiltersOrder.AUTHENTICATION);
        //无需认证的url
        List<String> ignoreAuthUrls = customSecurityProperties.getAllIgnoreAuthUrls();
        log.debug("【zeros-gateway】不需要【Token认证】的URL集合:{}", JacksonUtil.toJSONString(ignoreAuthUrls));

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
        authorizeExchange.pathMatchers(ignoreAuthUrls.toArray(new String[0])).permitAll();

        authorizeExchange
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange()
                .access(customAuthorizationManager)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(entryPoint)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .httpBasic().disable()
                .csrf().disable();
        return http.build();
    }

}
