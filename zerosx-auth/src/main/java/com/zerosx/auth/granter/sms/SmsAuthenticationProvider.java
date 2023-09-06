package com.zerosx.auth.granter.sms;

import com.zerosx.auth.service.userdetails.CustomUserDetailsChecker;
import com.zerosx.auth.service.userdetails.CustomUserDetailsServiceFactory;
import com.zerosx.common.security.tokens.SmsAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@Slf4j
@Getter
@Setter
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    private UserDetailsChecker detailsChecker = new CustomUserDetailsChecker();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        //手机号码
        String mobilePhone = (String) authentication.getPrincipal();
        UserDetails user = customUserDetailsServiceFactory.getDetailsService(authentication).loadUserByMobile(mobilePhone);
        detailsChecker.check(user);
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, user.getAuthorities());
        // 需要把未认证中的一些信息copy到已认证的token中
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
