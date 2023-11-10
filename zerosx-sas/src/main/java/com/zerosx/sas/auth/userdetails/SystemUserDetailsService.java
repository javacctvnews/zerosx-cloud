package com.zerosx.sas.auth.userdetails;

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
        CustomUserDetails customUserDetails = null;
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            userLoginDTO.setUsername(username);
            userLoginDTO.setMobilePhone(mobilePhone);
            ResultVO<LoginUserVO> loginAppUserResultVO = sysUserClient.queryLoginUser(userLoginDTO);
            LoginUserVO loginUserVO = loginAppUserResultVO.getData();
            customUserDetails = BeanCopierUtils.copyProperties(loginUserVO, CustomUserDetails.class);
            log.debug("用户名:{} {}", username, JacksonUtil.toJSONString(customUserDetails));
            if (loginUserVO != null) {
                ZerosSecurityContextHolder.setOperatorIds(loginUserVO.getOperatorId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return customUserDetails;
    }
}
