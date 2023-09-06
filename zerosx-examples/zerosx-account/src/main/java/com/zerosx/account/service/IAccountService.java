package com.zerosx.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.account.entity.Account;
import com.zerosx.api.examples.dto.AccountDTO;

public interface IAccountService extends IService<Account> {

    boolean decreaseAccount(AccountDTO accountDTO);
}
