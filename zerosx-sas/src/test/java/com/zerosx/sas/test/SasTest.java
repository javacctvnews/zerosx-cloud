package com.zerosx.sas.test;

import com.zerosx.common.sas.auth.CustomOAuth2AuthorizationService;
import com.zerosx.sas.SasApplication;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;

/**
 * SasTest
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-21 15:52
 **/
@SpringBootTest(classes = SasApplication.class)
public class SasTest {

    @Autowired
    private RegisteredClientRepository jdbcRegisteredClientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomOAuth2AuthorizationService customOAuth2AuthorizationService;

    @Test
    public void test01() {
        //OAuth2Authorization oAuth2Authorization = customOAuth2AuthorizationService.findById("1724411225560711203");
        OAuth2Authorization oAuth2Authorization = customOAuth2AuthorizationService.findByToken("1724419839377670209", OAuth2TokenType.ACCESS_TOKEN);
        OAuth2Authorization oAuth2Authorization2 = customOAuth2AuthorizationService.findByToken("1724411225560711203", OAuth2TokenType.ACCESS_TOKEN);
        System.out.println("oAuth2Authorization = " + oAuth2Authorization);
        System.out.println("oAuth2Authorization = " + oAuth2Authorization2);
    }

    @Test
    public void test() {
        Instant now = Instant.now();
        Instant now1 = now.plus(Duration.ofDays(300));
        RegisteredClient registeredClient = RegisteredClient.withId("saas")
                .clientId("saas")
                .clientIdIssuedAt(now)
                .clientSecretExpiresAt(now1)
                .clientName("SaaS")
                .clientSecret(passwordEncoder.encode("Zeros9999!#@"))
                .clientAuthenticationMethods(c -> {
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                    c.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                })
                .authorizationGrantTypes(g -> {
                    g.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    g.add(AuthorizationGrantType.REFRESH_TOKEN);
                    g.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    g.add(AuthorizationGrantType.PASSWORD);
                    g.add(AuthorizationGrantType.JWT_BEARER);
                    g.add(AuthorizationGrantType.DEVICE_CODE);
                    g.add(CustomAuthorizationGrantType.CAPTCHA_PWD);
                })
                .redirectUri("http://127.0.0.1:80/")
                .postLogoutRedirectUri("http://127.0.0.1:80/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.PHONE)
                .scope(OidcScopes.EMAIL)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .authorizationCodeTimeToLive(Duration.ofMinutes(2 * 60))
                        .accessTokenTimeToLive(Duration.ofMinutes(12 * 60))
                        //.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .deviceCodeTimeToLive(Duration.ofMinutes(12 * 60))
                        .reuseRefreshTokens(false)
                        .refreshTokenTimeToLive(Duration.ofMinutes(24 * 60))
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .build()
                )
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        jdbcRegisteredClientRepository.save(registeredClient);
    }

}
