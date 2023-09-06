package com.zerosx.api.system;

import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.factory.SysUserControllerFallbackFactory;
import com.zerosx.api.system.vo.SysUserVO;
import com.zerosx.common.base.constants.ServiceIdConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomUserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ServiceIdConstants.SYSTEM, contextId = ServiceIdConstants.SYSTEM, fallbackFactory = SysUserControllerFallbackFactory.class)
public interface ISysUserControllerApi {

    /**
     * 获取系统用户信息通过账号名称
     *
     * @return
     */
    @Deprecated
    @PostMapping("/sys_user/sysUserByUserName")
    ResultVO<SysUserVO> sysUserByUserName(@RequestParam("userName") String userName);


    /**
     * 获取系统用户信息通过账号名称
     *
     * @return LoginAppUser
     */
    @PostMapping("/sys_user/query_login_user")
    ResultVO<CustomUserDetails> queryLoginUser(@RequestBody UserLoginDTO userLoginDTO);


}
