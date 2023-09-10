package com.zerosx.sms.core.client;

import com.zerosx.sms.core.config.JdCloudConfig;
import com.zerosx.sms.model.SmsRequest;
import com.zerosx.sms.model.SmsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * JuheSmsClient
 * <p> 聚合短信服务商
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:38
 **/
@Slf4j
public class JdCloudSmsClient extends AbsMultiSmsClient {

    private final JdCloudConfig jdCloudConfig;

    public JdCloudSmsClient(JdCloudConfig jdCloudConfig) {
        this.jdCloudConfig = jdCloudConfig;
    }

    @Override
    public SmsResponse sendMessage(SmsRequest smsRequest) {
        //todo

        return SmsResponse.fail();
    }

}
