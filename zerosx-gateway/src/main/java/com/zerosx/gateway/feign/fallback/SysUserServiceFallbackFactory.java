package com.zerosx.gateway.feign.fallback;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.gateway.feign.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * userService降级工场
 */
@Slf4j
@Component
public class SysUserServiceFallbackFactory implements FallbackFactory<ISysUserService> {

    @Override
    public ISysUserService create(Throwable throwable) {
        return new ISysUserService() {
            @Override
            public ResultVO<LoginUserTenantsBO> currentLoginUser(String userName) {
                log.error("通过用户名查询用户租户集合异常:{}", userName, throwable);
                return ResultVOUtil.error(ResultEnum.FAIL.getCode(), "查询用户租户集合异常");
            }
        };
    }
}
