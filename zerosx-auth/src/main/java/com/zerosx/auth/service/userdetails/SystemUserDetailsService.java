package com.zerosx.auth.service.userdetails;

import com.zerosx.api.system.ISysUserClient;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
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
    private ISysUserClient sysUserClient;

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
        ResultVO<LoginUserVO> loginAppUserResultVO = sysUserClient.queryLoginUser(userLoginDTO);
        LoginUserVO loginUserVO = loginAppUserResultVO.getData();
        CustomUserDetails customUserDetails = BeanCopierUtils.copyProperties(loginUserVO, CustomUserDetails.class);
        log.debug("用户名:{} {}", username, JacksonUtil.toJSONString(customUserDetails));
        if (customUserDetails == null) {
            throw new DisabledException("用户不存在");
        }
        ZerosSecurityContextHolder.setOperatorIds(customUserDetails.getOperatorId());
        if (!customUserDetails.isEnabled()) {
            throw new DisabledException("用户已停用");
        }
        return customUserDetails;
    }
}
