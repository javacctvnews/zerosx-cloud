package com.zerosx.auth.service.userdetails;

import com.zerosx.api.system.ISysUserControllerApi;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.core.vo.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SystemUserDetailsService extends AbsCustomUserDetailsService {

    @Autowired
    private ISysUserControllerApi sysUserControllerApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getCustomUserDetails(username, null);
    }

    @Override
    public UserDetails loadUserByMobile(String mobilePhone) {
        return getCustomUserDetails("", mobilePhone);
    }

    private CustomUserDetails getCustomUserDetails(String username, String mobilePhone) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(username);
        userLoginDTO.setMobilePhone(mobilePhone);
        ResultVO<CustomUserDetails> loginAppUserResultVO = sysUserControllerApi.queryLoginUser(userLoginDTO);
        CustomUserDetails loginAppUser = loginAppUserResultVO.getData();
        log.debug("用户名:{} {}", username, JacksonUtil.toJSONString(loginAppUser));
        if (loginAppUser == null) {
            throw new DisabledException("用户不存在");
        }
        ZerosSecurityContextHolder.setOperatorIds(loginAppUser.getOperatorId());
        if (!loginAppUser.isEnabled()) {
            throw new DisabledException("用户已停用");
        }
        return loginAppUser;
    }
}
