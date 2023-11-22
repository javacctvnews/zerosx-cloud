package com.zerosx.resource.vo;

import lombok.Data;

@Data
public class SmsCodeVO {
    /**
     * 验证码
     */
    private String smsAuthCode;

    /**
     * 模拟发送短信
     */
    private Boolean imitate = false;
}
