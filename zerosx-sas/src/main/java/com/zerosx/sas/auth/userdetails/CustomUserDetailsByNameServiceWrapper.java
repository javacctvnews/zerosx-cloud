package com.zerosx.sas.auth.userdetails;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

/**
 * 重写UserDetailsByNameServiceWrapper
 */
public class CustomUserDetailsByNameServiceWrapper<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {

    private CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    public CustomUserDetailsByNameServiceWrapper() {
    }

    public CustomUserDetailsByNameServiceWrapper(final CustomUserDetailsServiceFactory customUserDetailsServiceFactory) {
        Assert.notNull(customUserDetailsServiceFactory, "customUserDetailsServiceFactory cannot be null.");
        this.customUserDetailsServiceFactory = customUserDetailsServiceFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.customUserDetailsServiceFactory, "customUserDetailsServiceFactory must be set");
    }

    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        ICustomUserDetailsService customUserDetailsService;
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            customUserDetailsService = customUserDetailsServiceFactory.getDetailsService((Authentication) authentication.getPrincipal());
        } else {
            customUserDetailsService = customUserDetailsServiceFactory.getDetailsService(authentication);
        }
        return customUserDetailsService.loadUserByUsername(authentication.getName());
    }

    public void setCustomUserDetailsServiceFactory(CustomUserDetailsServiceFactory customUserDetailsServiceFactory) {
        this.customUserDetailsServiceFactory = customUserDetailsServiceFactory;
    }

}
