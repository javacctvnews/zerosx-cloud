package com.zerosx.api.system;

import com.zerosx.api.system.dto.SmsSendDTO;
import com.zerosx.api.system.factory.SmsSupplierControllerFallbackFactory;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SmsSupplierControllerFallbackFactory.class)
public interface ISmsSupplierControllerApi {

    @PostMapping("/sms/send")
    ResultVO<?> sendSms(@RequestBody SmsSendDTO smsSendDTO);

}
