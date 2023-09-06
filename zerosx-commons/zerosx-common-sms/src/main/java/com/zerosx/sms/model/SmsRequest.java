package com.zerosx.sms.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class SmsRequest {
    /**
     * 运营商ID
     */
    private String operatorId;

    /**
     * 接受手机号码
     */
    private String phoneNumbers;
    /**
     * 短信签名
     */
    private String signature;
    /**
     * 模板code
     */
    public String templateCode;

    /**
     * 短信模板占位符参数，必须是对象的json格式(templateParam、params二选一，优先级高于params)
     */
    public String templateParam;

    /**
     * 短信模板占位符参数(templateParam、params二选一)
     */
    //Map<String, String> params = new LinkedHashMap<>();


}
