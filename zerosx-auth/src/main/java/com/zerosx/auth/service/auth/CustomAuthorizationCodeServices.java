package com.zerosx.auth.service.auth;

import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.properties.CustomSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final String AUTHORIZATION_CODE = "authorization_code:";
    //5分钟有效
    private static final long VALID_SECONDS = 300L;

    @Autowired
    private CustomSecurityProperties customSecurityProperties;
    @Autowired
    private RedissonOpService redissonOpService;

    /**
     * //5分钟有效
     *
     * @param code
     * @param authentication
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redissonOpService.set(code, authentication, VALID_SECONDS);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        Object obj = redissonOpService.get(code);
        if (obj != null) {
            redissonOpService.del(code);
            return (OAuth2Authentication) obj;
        }
        return null;
    }

}
