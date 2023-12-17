package com.zerosx.sas.auth.grant.sms;


import com.zerosx.common.sas.token.SmsAuthenticationToken;
import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationProvider;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.auth.grant.CustomUserDetailsChecker;
import com.zerosx.sas.auth.userdetails.CustomUserDetailsServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * SmsAuthenticationProvider
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:16
 **/
@Slf4j
public class SmsAuthenticationProvider extends CustomAbstractAuthenticationProvider<SmsAuthenticationToken> {

    private final CustomUserDetailsServiceFactory customUserDetailsServiceFactory;

    private final CustomUserDetailsChecker customUserDetailsChecker = new CustomUserDetailsChecker();

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailsServiceFactory customUserDetailsServiceFactory) {
        super(authorizationService, tokenGenerator);
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.customUserDetailsServiceFactory = customUserDetailsServiceFactory;
    }

    @Override
    protected Authentication executeAuthentication(Map<String, Object> reqParameters, Set<String> authorizedScopes) {
        UserDetails userDetails = customUserDetailsServiceFactory.getDetailsService((String) reqParameters.get(CustomOAuth2ParameterNames.USER_AUTH_TYPE))
                .loadUserByMobile((String) reqParameters.get(CustomOAuth2ParameterNames.PHONE));
        customUserDetailsChecker.check(userDetails);
        return new SmsAuthenticationToken(CustomAuthorizationGrantType.SMS, authorizedScopes, reqParameters, userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}