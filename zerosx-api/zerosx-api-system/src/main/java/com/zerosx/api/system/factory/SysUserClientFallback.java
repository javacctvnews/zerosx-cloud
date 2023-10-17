package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysUserClient;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SysUserClientFallback implements FallbackFactory<ISysUserClient> {

    @Override
    public ISysUserClient create(Throwable throwable) {

        return new ISysUserClient() {

            @Override
            public ResultVO<LoginUserVO> queryLoginUser(UserLoginDTO userLoginDTO) {
                log.error("查询用户异常：" + throwable.getMessage(), throwable.getMessage());
                return ResultVOUtil.emptyData();
            }

            @Override
            public ResultVO<LoginUserTenantsBO> currentLoginUser(String userName) {
                log.error("查询用户异常：" + throwable.getMessage(), throwable.getMessage());
                return ResultVOUtil.emptyData();
            }
        };
    }
}
