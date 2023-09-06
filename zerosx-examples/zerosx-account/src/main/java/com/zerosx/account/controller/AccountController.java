package com.zerosx.account.controller;

import com.zerosx.account.service.IAccountService;
import com.zerosx.api.examples.IAccountControllerApi;
import com.zerosx.api.examples.dto.AccountDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements IAccountControllerApi {

    @Autowired
    private IAccountService accountService;

    /**
     * 从账户扣钱
     */
    @PostMapping("/decrease_account")
    public ResultVO<?> decreaseAccount(@RequestBody AccountDTO accountDTO) {
        return ResultVOUtil.successBoolean(accountService.decreaseAccount(accountDTO));
    }

}
