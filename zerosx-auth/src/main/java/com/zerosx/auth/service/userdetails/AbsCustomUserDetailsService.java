package com.zerosx.auth.service.userdetails;

import com.zerosx.common.core.enums.AuthTypeEnum;

public abstract class AbsCustomUserDetailsService implements ICustomUserDetailsService {

    @Override
    public String authUserType() {
        return AuthTypeEnum.SYS_USER.getCode();
    }

}
