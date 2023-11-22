package com.zerosx.sas.auth.grant;

import com.zerosx.sas.auth.userdetails.CustomUserDetailsServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.Assert;

/**
 * 重写：DaoAuthenticationProvider
 * 用户名密码支持CustomUserDetailsServiceFactory
 */
@Slf4j
public class CustomPwdDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsChecker detailsChecker = new CustomUserDetailsChecker();

    public CustomPwdDaoAuthenticationProvider(CustomUserDetailsServiceFactory customUserDetailsServiceFactory, PasswordEncoder passwordEncoder) {
        this.customUserDetailsServiceFactory = customUserDetailsServiceFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new OAuth2AuthenticationException("凭证(credentials)不能为空");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new OAuth2AuthenticationException("用户名或密码错误");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails userDetails = customUserDetailsServiceFactory.getDetailsService(authentication).loadUserByUsername(username);
        detailsChecker.check(userDetails);
        return userDetails;
    }

    protected void doAfterPropertiesSet() {
        Assert.notNull(this.customUserDetailsServiceFactory, "CustomUserDetailsServiceFactory实例不能为空");
    }

}
