package com.zerosx.sas.auth.grant.pwd;


import com.zerosx.sas.utils.SasAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * PwdAuthenticationProvider
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:16
 **/
@Slf4j
public class PwdAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    private Supplier<String> refreshTokenGenerator;

    public PwdAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, AuthenticationManager authenticationManager) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PwdAuthenticationToken customAuthentication = (PwdAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = SasAuthUtils.getAuthenticatedClientElseThrowInvalidClient(customAuthentication);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        checkClient(registeredClient);

        Set<String> authorizedScopes;
        // Default to configured scopes
        if (!CollectionUtils.isEmpty(customAuthentication.getScopes())) {
            for (String requestedScope : customAuthentication.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(customAuthentication.getScopes());
        } else {
            authorizedScopes = new LinkedHashSet<>();
        }

        Map<String, Object> reqParameters = customAuthentication.getAdditionalParameters();
        /*try {*/

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(reqParameters);

        log.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

        Authentication usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // @formatter:off
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(usernamePasswordAuthentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizationGrant(customAuthentication);
            // @formatter:on

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizedScopes(authorizedScopes);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the access token.");
        }
        assert generatedAccessToken != null;
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.id(accessToken.getTokenValue())
                    .token(accessToken,
                            (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                    ((ClaimAccessor) generatedAccessToken).getClaims()))
                    // 0.4.0 新增的方法
                    .authorizedScopes(authorizedScopes)
                    .attribute(Principal.class.getName(), usernamePasswordAuthentication);
        } else {
            authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            if (this.refreshTokenGenerator != null) {
                Instant issuedAt = Instant.now();
                Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
                refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
            } else {
                tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    SasAuthUtils.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the refresh token.");
                }
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            }
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();

        this.authorizationService.save(authorization);

        log.debug("returning OAuth2AccessTokenAuthenticationToken");

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));

        /*} catch (Exception ex) {
            log.error("exception in authenticate: " + ex.getClass().getName(), ex);
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "", ""), ex);
        }*/
    }


    public void checkClient(RegisteredClient registeredClient) {
        assert registeredClient != null;
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }


    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        passwordAuthenticationToken.setDetails(reqParameters);
        return passwordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PwdAuthenticationToken.class.isAssignableFrom(authentication);
    }

}