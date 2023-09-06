package com.zerosx.sms.core.client;

import com.zerosx.common.base.utils.HttpClientUtils;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.sms.core.config.JuheConfig;
import com.zerosx.sms.model.SmsRequest;
import com.zerosx.sms.model.SmsResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * JuheSmsClient
 * <p> 聚合短信服务商
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:38
 **/
@Slf4j
public class JuheSmsClient extends AbsMultiSmsClient {

    private final JuheConfig juheConfig;

    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/x-www-form-urlencoded");
    }

    public JuheSmsClient(JuheConfig juheConfig) {
        this.juheConfig = juheConfig;
    }

    @Override
    public SmsResponse sendMessage(SmsRequest smsRequest) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile", smsRequest.getPhoneNumbers());
        paramMap.put("tpl_id", smsRequest.getTemplateCode());
        paramMap.put("vars", smsRequest.getTemplateParam());
        paramMap.put("key", juheConfig.getKey());
        paramMap.put("dtype", "json");
        try {
            String httpClientResult = HttpClientUtils.doPost(juheConfig.getDomainAddress(), headers, paramMap);
            Map<Object, Object> objectMap = JacksonUtil.toMap(httpClientResult);
            Integer errorCode = (Integer) objectMap.get("error_code");
            String reason = (String) objectMap.get("reason");
            log.debug("【{}】发送结果:{}", smsRequest.getPhoneNumbers(), httpClientResult);
            return new SmsResponse(errorCode, reason);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return SmsResponse.fail();
    }

}
