package com.zerosx.api.auth.factory;

import com.zerosx.api.auth.IAuthFeignServiceApi;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OauthFeignServiceFallbackFactory implements FallbackFactory<IAuthFeignServiceApi> {

    @Override
    public IAuthFeignServiceApi create(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return multiValueMap -> ResultVOUtil.error(ResultEnum.UNAUTHORIZED.getCode(), "认证授权失败");
    }

}
