package com.zerosx.api.resource;

import com.zerosx.api.resource.dto.SmsSendDTO;
import com.zerosx.api.resource.factory.SmsSupplierClientFallback;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "ISmsSupplierClient", name = ServiceIdConstants.RESOURCE, fallbackFactory = SmsSupplierClientFallback.class)
public interface ISmsSupplierClient {

    @PostMapping("/sms/send")
    ResultVO<?> sendSms(@RequestBody SmsSendDTO smsSendDTO);

}
