package com.zerosx.common.sas.token;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class SmsAuthenticationToken extends CustomAbstractAuthenticationToken {

    private final Object principal;

    public SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters, Object principal) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
        this.principal = principal;
    }

    public SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType, Set<String> scopes, Map<String, Object> additionalParameters, Object principal) {
        super(authorizationGrantType, null, scopes, additionalParameters);
        this.principal = principal;
    }

}
