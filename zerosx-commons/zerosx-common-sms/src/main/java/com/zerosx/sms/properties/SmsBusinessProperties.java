package com.zerosx.sms.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * CustomSmsProperties
 * <p> 可通过yaml配置sms，需自行实现
 *
 * @author: javacctvnews
 * @create: 2023-09-01 16:37
 **/
@Setter
@Getter
public class SmsBusinessProperties {

    /**
     * 短信业务编码
     */
    private String businessCode;

    /**
     * 短信模板编号
     */
    private String templateCode;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 模板签名 可空
     */
    private String signature;

}
