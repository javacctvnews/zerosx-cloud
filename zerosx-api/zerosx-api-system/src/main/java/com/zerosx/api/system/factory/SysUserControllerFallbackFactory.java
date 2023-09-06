package com.zerosx.api.system.factory;

import com.zerosx.api.system.ISysUserControllerApi;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.SysUserVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SysUserControllerFallbackFactory implements FallbackFactory<ISysUserControllerApi> {

    @Override
    public ISysUserControllerApi create(Throwable throwable) {

        return new ISysUserControllerApi() {
            @Override
            public ResultVO<SysUserVO> sysUserByUserName(String userName) {
                log.error("查询用户异常：" + throwable.getMessage(), throwable.getMessage());
                return ResultVOUtil.emptyData();
            }

            @Override
            public ResultVO<CustomUserDetails> queryLoginUser(UserLoginDTO userLoginDTO) {
                log.error("查询用户异常：" + throwable.getMessage(), throwable.getMessage());
                return ResultVOUtil.emptyData();
            }
        };
    }
}
