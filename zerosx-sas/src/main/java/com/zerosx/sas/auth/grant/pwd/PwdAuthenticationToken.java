package com.zerosx.sas.auth.grant.pwd;

import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * PwdAuthenticationToken
 * <p>
 * 密码授权token
 *
 * @author: javacctvnews
 * @create: 2023-10-23 16:30
 **/
public class PwdAuthenticationToken extends CustomAbstractAuthenticationToken {

    public PwdAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }

}
