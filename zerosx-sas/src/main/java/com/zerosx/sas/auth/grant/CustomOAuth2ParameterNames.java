package com.zerosx.sas.auth.grant;

/**
 * CustomOAuth2ParameterNames
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-28 18:32
 **/
public final class CustomOAuth2ParameterNames {

    private CustomOAuth2ParameterNames() {
    }

    /**
     * 授权用户类型字段
     */
    public static final String USER_AUTH_TYPE = "user_auth_type";

    /**
     * 验证码的序列Id
     */
    public static final String CAPTCHA_UUID = "uuid";
    /**
     * 手机号码
     */
    public static final String PHONE = "mobilePhone";
    /**
     * 短信验证码
     */
    public static final String SMS_CODE = "smsCode";

    /**
     * 客户端密码过期
     */
    public static final String CLIENT_SECRET_EXPIRES_AT =  "client_secret_expires_at";

}
