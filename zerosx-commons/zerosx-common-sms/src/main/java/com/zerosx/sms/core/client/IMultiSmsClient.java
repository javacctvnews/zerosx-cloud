package com.zerosx.sms.core.client;

import com.zerosx.sms.model.SmsResponse;
import com.zerosx.sms.model.SmsRequest;

/**
 * MultiSmsClient
 * <p> sms功能定义
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:03
 **/
public interface IMultiSmsClient {

    /**
     * 发送短信
     * @param smsRequest
     * @return
     */
    SmsResponse sendMessage(SmsRequest smsRequest);


}
