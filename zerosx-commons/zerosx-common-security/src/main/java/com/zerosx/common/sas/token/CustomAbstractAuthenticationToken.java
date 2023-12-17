package com.zerosx.common.sas.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * CustomAbstractAuthenticationToken
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 23:46
 **/
@Getter
public abstract class CustomAbstractAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthorizationGrantType authorizationGrantType;

    private final Authentication clientPrincipal;

    private final Set<String> scopes;

    protected final Map<String, Object> additionalParameters;

    public CustomAbstractAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = scopes;
        this.additionalParameters = additionalParameters;
        super.setDetails(additionalParameters);
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

    /**
     * 扩展模式一般不需要密码
     */
    @Override
    public Object getCredentials() {
        return "";
    }

}
