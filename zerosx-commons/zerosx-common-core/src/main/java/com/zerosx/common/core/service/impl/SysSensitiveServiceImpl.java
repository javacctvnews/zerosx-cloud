package com.zerosx.common.core.service.impl;

import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.ISensitiveService;
import org.springframework.stereotype.Service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 */
@Service
public class SysSensitiveServiceImpl implements ISensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive() {
        //return true;
        return !UserTypeEnum.SUPER_ADMIN.getCode().equals(ZerosSecurityContextHolder.getUserType());
    }

}
