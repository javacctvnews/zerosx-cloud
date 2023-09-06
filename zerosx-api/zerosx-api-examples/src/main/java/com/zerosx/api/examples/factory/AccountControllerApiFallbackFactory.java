package com.zerosx.api.examples.factory;

import com.zerosx.api.examples.IAccountControllerApi;
import com.zerosx.api.examples.dto.AccountDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountControllerApiFallbackFactory implements FallbackFactory<IAccountControllerApi> {

    @Override
    public IAccountControllerApi create(Throwable cause) {
        return new IAccountControllerApi() {
            @Override
            public ResultVO<?> decreaseAccount(AccountDTO accountDTO) {
                return ResultVOUtil.feignFail("账户扣减失败");
            }
        };
    }
}
