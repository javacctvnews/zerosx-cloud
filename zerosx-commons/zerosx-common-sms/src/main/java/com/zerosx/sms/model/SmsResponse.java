package com.zerosx.sms.model;

import lombok.Data;

@Data
public class SmsResponse {
    /**
     * 发送结果 0:成功，其他失败
     */
    private Integer sendCode;
    /**
     * 失败消息
     */
    private String sendMsg;
    /**
     * 接口响应的原始数据
     */
    private Object object;

    public SmsResponse() {
    }

    public SmsResponse(Integer sendCode) {
        this.sendCode = sendCode;
        this.sendMsg = "发送成功";
    }

    public SmsResponse(Integer sendCode, String sendMsg) {
        this.sendCode = sendCode;
        this.sendMsg = sendMsg;
    }

    public SmsResponse(Integer sendCode, String sendMsg, Object object) {
        this.sendCode = sendCode;
        this.sendMsg = sendMsg;
        this.object = object;
    }

    public static SmsResponse fail() {
        return new SmsResponse(1, "未知原因");
    }
}
