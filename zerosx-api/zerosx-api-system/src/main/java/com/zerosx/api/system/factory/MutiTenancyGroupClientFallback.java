package com.zerosx.api.system.factory;

import com.zerosx.api.system.IMutiTenancyGroupClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * MutiTenancyGroupControllerFallback
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-24 17:23
 **/
@Slf4j
@Component
public class MutiTenancyGroupClientFallback implements FallbackFactory<IMutiTenancyGroupClient> {

    @Override
    public IMutiTenancyGroupClient create(Throwable cause) {
        return new IMutiTenancyGroupClient() {
            @Override
            public ResultVO<String> transIdName(String operatorId) {
                log.error("查询运营商名称异常", cause);
                return ResultVOUtil.feignFail(cause.getMessage());
            }
        };
    }
}
