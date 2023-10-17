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

    /**
     * 默认的几种授权模式：authorization_code、implicit、client_credentials、password、REFRESH_TOKEN
     */

    AUTHORIZATION_CODE("authorization_code", "授权码模式"),

    IMPLICIT("implicit", "隐式授权模式"),

    CLIENT_CREDENTIALS("client_credentials", "客户端模式"),

    /**
     * 进行了多账户类型的扩展
     */
    PASSWORD("password", "密码模式"),

    REFRESH_TOKEN("refresh_token", "刷新令牌"),


    /**
     * 以下几种是额外扩展的授权模式
     */
    /**
     * 用户名密码+验证码模式
     */
    captcha("captcha", "密码验证码模式"),
    /**
     * 手机号码验证码模式
     */
    MOBILE_SMS("mobile_sms", "手机验证码模式"),
    ;

    private final String code;
    private final String message;

    GranterTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
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
