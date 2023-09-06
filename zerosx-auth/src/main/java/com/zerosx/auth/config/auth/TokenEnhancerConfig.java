package com.zerosx.auth.config.auth;

import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import com.zerosx.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TokenEnhancerConfig {

    @Autowired
    private CustomSecurityProperties customSecurityProperties;

    /*@Bean
    public TokenEnhancer tokenEnhancer(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(customSecurityProperties.getSigningKey());
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
        return jwtAccessTokenConverter;
    }*/

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
                return accessToken;
            }
            Authentication userAuthentication = authentication.getUserAuthentication();
            Map<String, Object> additionalInfo = new HashMap<>(3);
            String authUserType = SecurityUtils.getAuthUserType(userAuthentication);
            additionalInfo.put(SecurityConstants.USER_AUTH_TYPE, authUserType);
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) principal;
                additionalInfo.put("userName", customUserDetails.getUsername());
                additionalInfo.put("operatorId", customUserDetails.getOperatorId());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

}
