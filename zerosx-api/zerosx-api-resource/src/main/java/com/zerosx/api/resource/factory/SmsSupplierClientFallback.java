package com.zerosx.api.resource.factory;

import com.zerosx.api.resource.dto.SmsSendDTO;
import com.zerosx.api.resource.ISmsSupplierClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * SmsSupplierControllerFallbackFactory
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 11:39
 **/
@Component
@Slf4j
public class SmsSupplierClientFallback implements FallbackFactory<ISmsSupplierClient> {

    @Override
    public ISmsSupplierClient create(Throwable throwable) {
        return new ISmsSupplierClient() {
            @Override
            public ResultVO<?> sendSms(SmsSendDTO smsSendDTO) {
                log.error("短信发送失败：" + throwable.getMessage(), throwable);
                return ResultVOUtil.feignFail(throwable.getMessage());
            }
        };
    }
}
