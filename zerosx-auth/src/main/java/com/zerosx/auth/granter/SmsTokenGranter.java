package com.zerosx.auth.granter;

import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.core.enums.GranterTypeEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.security.tokens.SmsAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author javacctvnews
 * @description 手机号码+短信验证码授权模式
 * @date Created in 2023/8/10 17:34
 * @modify by
 */
public class SmsTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = GranterTypeEnum.MOBILE_SMS.getCode();

    private final AuthenticationManager authenticationManager;

    private final RedissonOpService redissonOpService;

    public SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                           OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager, RedissonOpService redissonOpService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.redissonOpService = redissonOpService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String smsCode = parameters.get("smsCode");
        String mobilePhone = parameters.get("mobilePhone");
        if (StringUtils.isBlank(mobilePhone)) {
            throw new InvalidGrantException("手机号码为空");
        }
        if (StringUtils.isBlank(smsCode)) {
            throw new InvalidGrantException("验证码为空");
        }
        String cacheCode = redissonOpService.get(ZCache.SMS_CODE.key(mobilePhone));
        if (StringUtils.isBlank(cacheCode)) {
            throw new InvalidGrantException("验证码不存在或已过期");
        }
        if (!smsCode.equals(cacheCode.toLowerCase())) {
            throw new InvalidGrantException("验证码不正确");
        }
        Authentication userAuth = new SmsAuthenticationToken(mobilePhone);
        // 当然该参数传null也行
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = authenticationManager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate mobile: " + mobilePhone);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
