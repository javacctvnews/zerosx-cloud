package com.zerosx.gateway.feign;

import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SysPermissionBO;
import com.zerosx.gateway.feign.fallback.SysUserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 系统用户feign
 * @Author javacctvnews
 * @Date 2023/3/13 13:26
 */
@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, decode404 = true, fallbackFactory = SysUserServiceFallbackFactory.class)
public interface ISysUserService {

    /**
     * 查询用户的租户ID信息
     *
     * @param userName
     * @return
     */
    @PostMapping("/sys_user/current_login_user")
    ResultVO<LoginUserTenantsBO> currentLoginUser(@RequestParam("userName") String userName);

}
