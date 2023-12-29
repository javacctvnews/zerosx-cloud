package com.zerosx.gateway.config;


import com.zerosx.api.system.ISysUserClient;
import com.zerosx.common.log.properties.CustomLogProperties;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.auth.CustomOAuth2AuthorizationService;
import com.zerosx.common.sas.properties.CustomSecurityProperties;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.gateway.auth.CustomAuthenticationManager;
import com.zerosx.gateway.auth.CustomAuthorizationManager;
import com.zerosx.gateway.auth.CustomServerWebExchangeMatcher;
import com.zerosx.gateway.filter.CustomServerWebExchangeContextFilter;
import com.zerosx.gateway.filter.TraceIDFilter;
import com.zerosx.gateway.handler.CustomAccessDeniedHandler;
import com.zerosx.gateway.handler.CustomAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
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
    private CustomAuthorizationManager customAuthorizationManager;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private CustomLogProperties customLogProperties;
    @Autowired
    private CustomOAuth2AuthorizationService customOAuth2AuthorizationService;
    @Lazy
    @Autowired
    private ISysUserClient sysUserClient;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain() {
        ServerHttpSecurity http = ServerHttpSecurity.http();
        http.addFilterAt(new TraceIDFilter(customLogProperties), SecurityWebFiltersOrder.FIRST);
        http.addFilterAt(new CustomServerWebExchangeContextFilter(customOAuth2AuthorizationService, customSecurityProperties, redissonOpService, sysUserClient), SecurityWebFiltersOrder.FIRST);
        //认证处理器
        ReactiveAuthenticationManager customAuthenticationManager = new CustomAuthenticationManager(customOAuth2AuthorizationService);
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        //token转换器
        ServerBearerTokenAuthenticationConverter tokenAuthenticationConverter = new ServerBearerTokenAuthenticationConverter();
        tokenAuthenticationConverter.setAllowUriQueryParameter(true);
        //oauth2认证过滤器
        AuthenticationWebFilter oauth2Filter = new AuthenticationWebFilter(customAuthenticationManager);
        oauth2Filter.setRequiresAuthenticationMatcher(new CustomServerWebExchangeMatcher(customSecurityProperties));
        oauth2Filter.setServerAuthenticationConverter(tokenAuthenticationConverter);
        oauth2Filter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
        http.addFilterAt(oauth2Filter, SecurityWebFiltersOrder.AUTHENTICATION);
        //无需认证的url
        List<String> ignoreAuthUrls = customSecurityProperties.getAllIgnoreAuthUrls();
        log.debug("【zeros-gateway】不需要【Token认证】的URL集合:{}", JacksonUtil.toJSONString(ignoreAuthUrls));
        http.authorizeExchange((authorize) -> authorize
                        .pathMatchers(ignoreAuthUrls.toArray(new String[0])).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange()
                        .access(customAuthorizationManager))
                .exceptionHandling((except) -> except
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(entryPoint))
                .headers((header) -> header.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }


}
