package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISmsSupplierControllerApi;
import com.zerosx.api.system.dto.SmsSendDTO;
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
public class SmsSupplierControllerFallbackFactory implements FallbackFactory<ISmsSupplierControllerApi> {

    @Override
    public ISmsSupplierControllerApi create(Throwable throwable) {
        return new ISmsSupplierControllerApi() {
            @Override
            public ResultVO<?> sendSms(SmsSendDTO smsSendDTO) {
                log.error("短信发送失败：" + throwable.getMessage(), throwable.getMessage());
                return ResultVOUtil.emptyData();
            }
        };
    }
}
