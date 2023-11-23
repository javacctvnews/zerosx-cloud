package com.zerosx.sas.auth.grant;

import com.zerosx.common.sas.token.CustomAbstractAuthenticationToken;
import com.zerosx.sas.utils.SasAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * CustomAuthenticationProvider
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 23:50
 **/
@Slf4j
public abstract class CustomAbstractAuthenticationProvider<T extends CustomAbstractAuthenticationToken> implements AuthenticationProvider {

    private final OAuth2AuthorizationService authorizationService;

    protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    protected CustomAbstractAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        T customAuthentication = (T) authentication;

        Map<String, Object> reqParameters = customAuthentication.getAdditionalParameters();

        AuthorizationGrantType grantType = grantType(reqParameters);

        OAuth2ClientAuthenticationToken clientPrincipal = SasAuthUtils.getAuthenticatedClientElseThrowInvalidClient(customAuthentication);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        checkRegisteredClient(registeredClient, grantType);

        Set<String> authorizedScopes = getAuthorizedScopes(customAuthentication.getScopes(), registeredClient.getScopes());

        Authentication authenticationed = executeAuthentication(reqParameters, authorizedScopes);

        return buildOAuth2Authorization(customAuthentication, authenticationed, registeredClient,
                authorizedScopes, grantType, clientPrincipal);
    }

    protected abstract Authentication executeAuthentication(Map<String, Object> reqParameters, Set<String> authorizedScopes);

    protected AuthorizationGrantType grantType(Map<String, Object> reqParameters){
        String grantType = (String) reqParameters.get(OAuth2ParameterNames.GRANT_TYPE);
        if (StringUtils.isBlank(grantType)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }
        AuthorizationGrantType type = CustomAuthorizationGrantType.getGrantType(grantType);
        if (type == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }
        return type;
    }

    protected Authentication buildOAuth2Authorization(Authentication customAuthentication, Authentication authentication,
                                                      RegisteredClient registeredClient, Set<String> authorizedScopes,
                                                      AuthorizationGrantType authorizationGrantType, OAuth2ClientAuthenticationToken clientPrincipal) {
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(authorizationGrantType)
                .authorizationGrant(customAuthentication);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(authentication.getName())
                .authorizationGrantType(authorizationGrantType)
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
                    .authorizedScopes(authorizedScopes)
                    .attribute(Principal.class.getName(), authentication);
        } else {
            authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
        }
        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                SasAuthUtils.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the refresh token.");
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();

        this.authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));
    }

    /**
     * 客户端检查
     *
     * @param registeredClient
     * @param authorizationGrantType
     */
    protected void checkRegisteredClient(RegisteredClient registeredClient, AuthorizationGrantType authorizationGrantType) {
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }
        if (authorizationGrantType == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }
        if (!registeredClient.getAuthorizationGrantTypes().contains(authorizationGrantType)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    protected Set<String> getAuthorizedScopes(Set<String> tokenScopes, Set<String> clientScopes) {
        Set<String> authorizedScopes;
        if (!CollectionUtils.isEmpty(tokenScopes)) {
            for (String requestedScope : tokenScopes) {
                if (!clientScopes.contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(tokenScopes);
        } else {
            authorizedScopes = new LinkedHashSet<>();
        }
        return authorizedScopes;
    }

    protected UsernamePasswordAuthenticationToken buildPwdToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        passwordAuthenticationToken.setDetails(reqParameters);
        return passwordAuthenticationToken;
    }

}
