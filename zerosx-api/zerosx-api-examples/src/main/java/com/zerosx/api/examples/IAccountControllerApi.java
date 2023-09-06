package com.zerosx.api.examples;

import com.zerosx.api.examples.dto.AccountDTO;
import com.zerosx.api.examples.factory.AccountControllerApiFallbackFactory;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zerosx-account", contextId = "zerosx-account", fallbackFactory = AccountControllerApiFallbackFactory.class)
public interface IAccountControllerApi {

    /**
     * 从账户扣钱
     */
    @PostMapping("/decrease_account")
    ResultVO<?> decreaseAccount(@RequestBody AccountDTO accountDTO);

}
