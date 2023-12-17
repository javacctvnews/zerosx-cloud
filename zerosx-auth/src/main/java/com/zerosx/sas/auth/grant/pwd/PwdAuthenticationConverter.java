package com.zerosx.sas.auth.grant.pwd;

import com.zerosx.common.sas.token.PwdAuthenticationToken;
import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationConverter;
import com.zerosx.sas.utils.SasAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * PwdAuthenticationConverter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:10
 **/
@Slf4j
public class PwdAuthenticationConverter extends CustomAbstractAuthenticationConverter<PwdAuthenticationToken> {

    protected boolean checkGrantType(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    public void checkRequestParams(MultiValueMap<String, String> parameters) {
        // username必填
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME);
        }
        // password必填
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD);
        }
    }

    protected PwdAuthenticationToken buildAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new PwdAuthenticationToken(AuthorizationGrantType.PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
    }

}
