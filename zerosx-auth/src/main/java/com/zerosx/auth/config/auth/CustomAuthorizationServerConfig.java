package com.zerosx.auth.config.auth;

import com.zerosx.auth.granter.CaptchaTokenGranter;
import com.zerosx.auth.granter.SmsTokenGranter;
import com.zerosx.auth.service.auth.CustomAuthorizationCodeServices;
import com.zerosx.auth.service.auth.CustomDefaultTokenServices;
import com.zerosx.auth.service.auth.CustomJdbcClientDetailsService;
import com.zerosx.auth.service.userdetails.CustomUserDetailsByNameServiceWrapper;
import com.zerosx.auth.service.userdetails.CustomUserDetailsServiceFactory;
import com.zerosx.auth.service.userdetails.SystemUserDetailsService;
import com.zerosx.common.redis.templete.RedissonOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private CustomJdbcClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomAuthorizationCodeServices customAuthorizationCodeServices;
    @Autowired
    private SystemUserDetailsService systemUserDetailsService;
    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    @Autowired
    private TokenEnhancer tokenEnhancer;
    @Resource
    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;
    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //默认的4种授权模式
        List<TokenGranter> tokenGranters = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        //增加自定义的授权模式
        //验证码（用户名+密码+验证码）授权模式
        CaptchaTokenGranter captchaTokenGranter = new CaptchaTokenGranter(tokenService(), clientDetailsService, endpoints.getOAuth2RequestFactory(),
                authenticationManager, redissonOpService);
        tokenGranters.add(captchaTokenGranter);
        //sms 授权模式
        SmsTokenGranter smsTokenGranter = new SmsTokenGranter(tokenService(), clientDetailsService, endpoints.getOAuth2RequestFactory(),
                authenticationManager, redissonOpService);
        tokenGranters.add(smsTokenGranter);

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(tokenGranters);
        endpoints
                //.pathMapping("/oauth/token", "/oauth/login")//path映射
                .authenticationManager(authenticationManager) //授权管理器
                .userDetailsService(systemUserDetailsService)
                .tokenStore(tokenStore) //令牌存储
                .authorizationCodeServices(customAuthorizationCodeServices)//授权码
                .tokenServices(tokenService())
                .tokenGranter(compositeTokenGranter)
                .exceptionTranslator(webResponseExceptionTranslator)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .reuseRefreshTokens(true);
        ;
    }

    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices tokenServices = new CustomDefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);//token存储实现
        tokenServices.setSupportRefreshToken(true);//支持刷新token
        tokenServices.setClientDetailsService(clientDetailsService);//客户端详情实现
        tokenServices.setTokenEnhancer(tokenEnhancer);
        //authenticationManager
        if (this.customUserDetailsServiceFactory != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(
                    new CustomUserDetailsByNameServiceWrapper<>(this.customUserDetailsServiceFactory));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
        return tokenServices;
    }

    /**
     * 配置令牌端点的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //让端点"/oauth/token"支持client_id以及client_secret作登录认证
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端详情
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

}
