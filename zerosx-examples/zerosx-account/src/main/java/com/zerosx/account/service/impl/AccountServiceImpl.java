package com.zerosx.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.account.entity.Account;
import com.zerosx.account.mapper.IAccountMapper;
import com.zerosx.account.service.IAccountService;
import com.zerosx.api.examples.dto.AccountDTO;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<IAccountMapper, Account> implements IAccountService {

    @Override
    public boolean decreaseAccount(AccountDTO accountDTO) {
        log.info("全局事务id:{}", RootContext.getXID());
        LambdaQueryWrapper<Account> qw = Wrappers.lambdaQuery(Account.class);
        qw.eq(Account::getUserId, accountDTO.getUserId());
        Account dbAccount = getOne(qw);
        dbAccount.setAmount(BigDecimal.valueOf(dbAccount.getAmount()).subtract(accountDTO.getAmount()).doubleValue());
        return updateById(dbAccount);
    }

}
