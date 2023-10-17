package com.zerosx.api.auth.factory;

import com.zerosx.api.auth.IAccessTokenClient;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
@Slf4j
public class AccessTokenClientFallback implements FallbackFactory<IAccessTokenClient> {

    @Override
    public IAccessTokenClient create(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return new IAccessTokenClient() {
            @Override
            public ResultVO postAccessToken(MultiValueMap<String, String> multiValueMap) {
                return ResultVOUtil.error(ResultEnum.UNAUTHORIZED.getCode(), "认证授权失败");
            }

            @Override
            public ResultVO<OauthClientDetailsBO> getClient(String clientId) {
                return ResultVOUtil.emptyData();
            }
        };
    }

}
