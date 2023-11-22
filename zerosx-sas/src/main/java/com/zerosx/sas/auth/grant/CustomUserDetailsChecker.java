package com.zerosx.sas.auth.grant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * CustomUserDetailsChecker
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-01 12:55
 **/
@Slf4j
public class CustomUserDetailsChecker implements UserDetailsChecker {

    @Override
    public void check(UserDetails userDetails) {
        log.debug("执行CustomUserDetailsChecker");
        if (userDetails == null) {
            throw new OAuth2AuthenticationException("用户不存在");
        }
        if (!userDetails.isAccountNonLocked()) {
            throw new OAuth2AuthenticationException("用户被锁定");
        }
        if (!userDetails.isEnabled()) {
            throw new OAuth2AuthenticationException("用户已停用");
        }
        if (!userDetails.isAccountNonExpired()) {
            throw new OAuth2AuthenticationException("用户已过期");
        }
    }
}
