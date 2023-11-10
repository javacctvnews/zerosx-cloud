package com.zerosx.gateway.auth;

import com.zerosx.common.sas.auth.CustomOAuth2AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;

import java.security.Principal;


@Slf4j
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    private final CustomOAuth2AuthorizationService customOAuth2AuthorizationService;

    public CustomAuthenticationManager(CustomOAuth2AuthorizationService customOAuth2AuthorizationService) {
        this.customOAuth2AuthorizationService = customOAuth2AuthorizationService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication).filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class).map(BearerTokenAuthenticationToken::getToken)
                .flatMap((tokenValue -> {
                    log.debug("执行校验【token】有效性:{}", tokenValue);
                    OAuth2Authorization oAuth2Authorization = customOAuth2AuthorizationService.findByToken(tokenValue, OAuth2TokenType.ACCESS_TOKEN);
                    if (oAuth2Authorization == null) {
                        return Mono.error(new InvalidBearerTokenException("无效的会话，或者会话已过期"));
                    }
                    Authentication result = oAuth2Authorization.getAttribute(Principal.class.getName());
                    if (result == null) {
                        return Mono.error(new InvalidBearerTokenException("无效的会话，或者会话已过期"));
                    }
                    return Mono.just(result);
                })).cast(Authentication.class);
    }
}
