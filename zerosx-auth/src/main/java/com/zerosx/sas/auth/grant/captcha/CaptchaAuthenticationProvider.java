package com.zerosx.sas.auth.grant.captcha;


import com.zerosx.common.sas.token.CaptchaAuthenticationToken;
import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * CaptchaAuthenticationProvider
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:16
 **/
@Slf4j
public class CaptchaAuthenticationProvider extends CustomAbstractAuthenticationProvider<CaptchaAuthenticationToken> {

    private final AuthenticationManager authenticationManager;

    public CaptchaAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, AuthenticationManager authenticationManager) {
        super(authorizationService, tokenGenerator);
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected Authentication executeAuthentication(Map<String, Object> reqParameters, Set<String> authorizedScopes) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildPwdToken(reqParameters);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }
}