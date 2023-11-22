package com.zerosx.sas.auth.grant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Set;

/**
 * CustomAuthorizationGrantType
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-28 18:03
 **/
public final class CustomAuthorizationGrantType {

    /*默认的几种*/
    public static final AuthorizationGrantType AUTHORIZATION_CODE = new AuthorizationGrantType("authorization_code");
    public static final AuthorizationGrantType REFRESH_TOKEN = new AuthorizationGrantType("refresh_token");
    public static final AuthorizationGrantType CLIENT_CREDENTIALS = new AuthorizationGrantType("client_credentials");

    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");
    public static final AuthorizationGrantType JWT_BEARER = new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:jwt-bearer");
    public static final AuthorizationGrantType DEVICE_CODE = new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:device_code");

    /*以下是扩展的*/

    /**
     * 认证模式：用户名、密码 + 验证码
     */
    public static final AuthorizationGrantType CAPTCHA_PWD = new AuthorizationGrantType("captcha_pwd");

    /**
     * 认证模式：手机号码+短信验证码
     */
    public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms");


    private static final Set<AuthorizationGrantType> types = Set.of(
            AUTHORIZATION_CODE,
            REFRESH_TOKEN,
            CLIENT_CREDENTIALS,
            PASSWORD, JWT_BEARER,
            DEVICE_CODE,
            CAPTCHA_PWD,
            SMS);

    public static AuthorizationGrantType getGrantType(String grantType) {
        for (AuthorizationGrantType type : types) {
            if (type.getValue().equals(grantType)) {
                return type;
            }
        }
        return null;
    }

}
