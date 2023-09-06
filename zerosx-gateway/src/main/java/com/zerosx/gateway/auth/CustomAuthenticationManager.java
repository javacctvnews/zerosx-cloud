package com.zerosx.gateway.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;


@Slf4j
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    private TokenStore tokenStore;

    public CustomAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication).filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class).map(BearerTokenAuthenticationToken::getToken)
                .flatMap((tokenValue -> {
                    log.debug("执行校验【token】有效性:{}", tokenValue);
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
                    if (accessToken == null || accessToken.isExpired()) {
                        tokenStore.removeAccessToken(accessToken);
                        return Mono.error(new InvalidTokenException("无效的会话，或者会话已过期"));
                    }
                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
                    if (result == null) {
                        return Mono.error(new InvalidTokenException("无效的会话，或者会话已过期"));
                    }
                    return Mono.just(result);
                })).cast(Authentication.class);
    }
}
