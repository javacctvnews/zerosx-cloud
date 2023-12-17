package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * 授权类型
 */
@Getter
@AutoDictData(name = "授权类型")
public enum GranterTypeEnum implements BaseEnum<String> {

    AUTHORIZATION_CODE("authorization_code", "授权码"),

    /*IMPLICIT("implicit", "隐式授权模式"),*/

    CLIENT_CREDENTIALS("client_credentials", "客户端"),
    JWT_BEARER("urn:ietf:params:oauth:grant-type:jwt-bearer", "JwtBearer"),
    DEVICE_CODE("urn:ietf:params:oauth:grant-type:device_code", "设备码"),

    /**
     * 进行了多账户类型的扩展
     */
    PASSWORD("password", "用户名密码"),

    REFRESH_TOKEN("refresh_token", "刷新令牌"),


    /**
     * 以下几种是额外扩展的授权模式
     */
    /**
     * 用户名密码+验证码模式
     */
    captcha("captcha_pwd", "密码验证码"),
    /**
     * 手机号码验证码模式
     */
    MOBILE_SMS("sms", "手机验证码"),
    ;

    private final String code;
    private final String message;

    GranterTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

    public static String getMessageByCode(String obj) {
        GranterTypeEnum[] values = GranterTypeEnum.values();
        for (GranterTypeEnum value : values) {
            if (value.getCode().equals(obj)) {
                return value.getMessage();
            }
        }
        return "";
    }

}
