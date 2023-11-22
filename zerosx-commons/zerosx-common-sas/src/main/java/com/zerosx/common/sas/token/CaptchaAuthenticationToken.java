package com.zerosx.common.sas.token;

import com.zerosx.common.sas.token.CustomAbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * CaptchaAuthenticationToken
 * <p>
 * 密码授权token
 *
 * @author: javacctvnews
 * @create: 2023-10-23 16:30
 **/
public class CaptchaAuthenticationToken extends CustomAbstractAuthenticationToken {

    public CaptchaAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }

}
