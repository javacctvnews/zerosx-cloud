package com.zerosx.auth.service.userdetails;

import com.zerosx.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@Slf4j
public class CustomUserDetailsChecker implements UserDetailsChecker {

    @Override
    public void check(UserDetails userDetails) {
        log.debug("执行【UserDetailsChecker】:{}", JacksonUtil.toJSONString(userDetails));
    }

}
