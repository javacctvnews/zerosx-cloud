package com.zerosx.sms.core.client;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import com.zerosx.sms.model.SmsResponse;
import com.zerosx.sms.model.SmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * AlibabaSmsClient
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:38
 **/
@Slf4j
public class AlibabaSmsClient extends AbsMultiSmsClient {

    private final AlibabaConfig alibabaConfig;

    private final Client client;

    public AlibabaSmsClient(AlibabaConfig alibabaConfig) {
        this.alibabaConfig = alibabaConfig;
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(alibabaConfig.getAccessKeyId())
                .setAccessKeySecret(alibabaConfig.getAccessKeySecret());
        config.endpoint = alibabaConfig.getRegionId();
        try {
            client = new Client(config);
            log.debug("【com.aliyun.dysmsapi20170525.Client】加载成功");
        } catch (Exception e) {
            throw new RuntimeException("【com.aliyun.dysmsapi20170525.Client】加载失败：" + e.getMessage(), e);
        }
    }

    @Override
    public SmsResponse sendMessage(SmsRequest smsSendDTO) {
        log.debug("调用【{}】发送短信:{} 参数:{}", SupplierTypeEnum.ALIBABA.getCode(), smsSendDTO.getPhoneNumbers(), smsSendDTO.getTemplateParam());
        SendSmsRequest smsRequest = new SendSmsRequest()
                .setPhoneNumbers(smsSendDTO.getPhoneNumbers())
                .setSignName(StringUtils.isBlank(smsSendDTO.getSignature()) ? alibabaConfig.getSignature() : smsSendDTO.getSignature())
                .setTemplateCode(smsSendDTO.getTemplateCode())
                .setTemplateParam(smsSendDTO.getTemplateParam());
        try {
            SendSmsResponse sendSmsResponse = client.sendSms(smsRequest);
            SendSmsResponseBody smsResponseBody = sendSmsResponse.getBody();
            log.debug("【{}】发送结果:{}", smsSendDTO.getTemplateCode(), smsResponseBody.getMessage());
            if ("OK".equalsIgnoreCase(smsResponseBody.getCode())) {
                return new SmsResponse(0);
            } else {
                return new SmsResponse(1, smsResponseBody.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SmsResponse(1, "发送失败，原因：" + e.getMessage());
        }
    }


}
