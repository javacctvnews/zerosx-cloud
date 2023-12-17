package com.zerosx.sas.auth.config;

import com.zerosx.sas.auth.grant.CustomPwdDaoAuthenticationProvider;
import com.zerosx.sas.auth.token.CustomOAuth2RefreshTokenGenerator;
import com.zerosx.sas.auth.token.CustomOAuth2TokenCustomizer;
import com.zerosx.sas.auth.token.CustomOAuth2TokenGenerator;
import com.zerosx.sas.auth.userdetails.CustomUserDetailsServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * SasBeanConfig
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-24 08:58
 **/
@Configuration
public class SasAuthBeanConfig {

    /**
     * 令牌生成规则实现 </br>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator() {
        CustomOAuth2TokenGenerator accessTokenGenerator = new CustomOAuth2TokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new CustomOAuth2RefreshTokenGenerator());
    }

    /**
     * 重写 DaoAuthenticationProvider
     *
     * @param customUserDetailsServiceFactory
     * @param passwordEncoder
     * @return
     */
    @Bean
    public CustomPwdDaoAuthenticationProvider customDaoAuthenticationProvider(CustomUserDetailsServiceFactory customUserDetailsServiceFactory,
                                                                              PasswordEncoder passwordEncoder) {
        return new CustomPwdDaoAuthenticationProvider(customUserDetailsServiceFactory, passwordEncoder);
    }

    /**
     * RegisteredClient Client客户端
     *
     * @param jdbcTemplate
     * @return
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 将AuthenticationManager注入ioc中，其它需要使用地方可以直接从ioc中获取
     *
     * @param authenticationConfiguration 导出认证配置
     * @return AuthenticationManager 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JdbcRegisteredClientRepository.RegisteredClientParametersMapper registeredClientParametersMapper() {
        return new JdbcRegisteredClientRepository.RegisteredClientParametersMapper();
    }
}
