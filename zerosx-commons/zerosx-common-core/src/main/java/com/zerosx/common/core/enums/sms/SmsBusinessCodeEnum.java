package com.zerosx.common.core.enums.sms;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * sms业务编码
 */
@Getter
@AutoDictData(name = "短信业务编码")
public enum SmsBusinessCodeEnum implements BaseEnum<String> {

    VERIFY_CODE("verify_code", "验证码"),

    TENANT_EXPIRE_NOTICE("tenant_expire_notice", "租户到期通知"),
    ;

    private final String code;

    private final String message;

    SmsBusinessCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getByCode(String code) {
        SmsBusinessCodeEnum[] values = SmsBusinessCodeEnum.values();
        for (SmsBusinessCodeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return "";
    }


}
