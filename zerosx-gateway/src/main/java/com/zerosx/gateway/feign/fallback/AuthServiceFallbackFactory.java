package com.zerosx.gateway.feign.fallback;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.gateway.feign.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * IAuthService降级工场
 */
@Slf4j
@Component
public class AuthServiceFallbackFactory implements FallbackFactory<IAuthService> {

    @Override
    public IAuthService create(Throwable cause) {
        return new IAuthService() {
            @Override
            public ResultVO<OauthClientDetailsBO> getClient(String clientId) {
                log.error(cause.getMessage(), cause);
                return ResultVOUtil.emptyData();
            }
        };
    }
}
