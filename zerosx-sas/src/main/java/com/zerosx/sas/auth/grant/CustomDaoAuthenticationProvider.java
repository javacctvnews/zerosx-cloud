package com.zerosx.sas.auth.grant;

import com.zerosx.sas.auth.userdetails.CustomUserDetailsServiceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.Assert;

/**
 * 重写：DaoAuthenticationProvider
 * 用户名密码支持CustomUserDetailsServiceFactory
 */
@Slf4j
@Getter
@Setter
public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    private PasswordEncoder passwordEncoder;

    private UserDetailsPasswordService userDetailsPasswordService;

    private UserDetailsChecker detailsChecker = new CustomUserDetailsChecker();

    public void setCustomUserDetailsServiceFactory(CustomUserDetailsServiceFactory customUserDetailsServiceFactory) {
        this.customUserDetailsServiceFactory = customUserDetailsServiceFactory;
    }

    public CustomDaoAuthenticationProvider() {

    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Failed to authenticate since no credentials provided");
            throw new OAuth2AuthenticationException("Failed to authenticate since no credentials provided");
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                logger.warn("Failed to authenticate since password does not match stored value");
                throw new OAuth2AuthenticationException("用户名或密码错误");
            }
        }
    }

    protected void doAfterPropertiesSet() {
        Assert.notNull(this.customUserDetailsServiceFactory, "A customUserDetailsServiceFactory must be set");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails userDetails = customUserDetailsServiceFactory.getDetailsService(authentication).loadUserByUsername(username);
        detailsChecker.check(userDetails);
        return userDetails;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

}
