package com.zerosx.api.system;

import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.factory.SysUserClientFallback;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "ISysUserClient", name = ServiceIdConstants.SYSTEM, fallbackFactory = SysUserClientFallback.class)
public interface ISysUserClient {

    /**
     * 获取系统用户信息通过账号名称
     *
     * @return LoginAppUser
     */
    @PostMapping("/sys_user/query_login_user")
    ResultVO<LoginUserVO> queryLoginUser(@RequestBody UserLoginDTO userLoginDTO);

    /**
     * 查询用户的租户ID信息
     *
     * @param userName
     * @return
     */
    @PostMapping("/sys_user/current_login_user")
    ResultVO<LoginUserTenantsBO> currentLoginUser(@RequestParam("userName") String userName);


}
