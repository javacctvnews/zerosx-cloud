package com.zerosx.sas.utils;

import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2DeviceCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SasAuthUtils {

    public static final String DEFAULT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    /**
     * 获取用户类型
     *
     * @param authentication
     * @return
     */
    public static String getAuthUserType(Authentication authentication) {
        if (authentication == null || authentication.getDetails() == null) {
            return "";
        }
        Map<String, String> detailsMap = (Map<String, String>) authentication.getDetails();
        return detailsMap.get(CustomOAuth2ParameterNames.USER_AUTH_TYPE);
    }

    public static void throwError(String errorCode, String parameterName) {
        throwError(errorCode, parameterName, DEFAULT_ERROR_URI);
    }

    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
    }

    /**
     * 从认证信息中获取客户端token
     *
     * @param authentication 认证信息
     * @return 客户端认证信息，获取失败抛出异常
     */
    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                //log.debug("{} {}", key, value);
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    public static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
        List<AuthenticationConverter> authenticationConverters = new ArrayList();
        authenticationConverters.add(new OAuth2AuthorizationCodeAuthenticationConverter());
        authenticationConverters.add(new OAuth2RefreshTokenAuthenticationConverter());
        authenticationConverters.add(new OAuth2ClientCredentialsAuthenticationConverter());
        authenticationConverters.add(new OAuth2DeviceCodeAuthenticationConverter());
        return authenticationConverters;
    }

    /**
     * 生成token
     *
     * @return token值
     */
    public static String tokenValue() {
        //return UUID.randomUUID().toString().replace("-", "") + IdGenerator.getIdStr();
        return IdGenerator.getIdStr();
    }
}
