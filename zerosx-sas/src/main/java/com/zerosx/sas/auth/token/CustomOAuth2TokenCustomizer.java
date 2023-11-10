package com.zerosx.sas.auth.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Slf4j
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        log.debug("添加自定义内容输出");
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        claims.claim(OAuth2ParameterNames.CLIENT_ID, context.getAuthorizationGrant().getName());
        claims.claim(OAuth2ParameterNames.GRANT_TYPE, context.getAuthorizationGrantType().getValue());
    }

}
