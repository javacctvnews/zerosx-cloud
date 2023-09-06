package com.zerosx.auth.granter;

import com.zerosx.auth.service.IVerificationCodeService;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.security.tokens.SmsAuthenticationToken;
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

    private static final String GRANT_TYPE = "mobile_sms";

    private final AuthenticationManager authenticationManager;

    private final IVerificationCodeService verificationCodeService;

    public SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                           OAuth2RequestFactory requestFactory,AuthenticationManager authenticationManager,
                           IVerificationCodeService verificationCodeService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
        this.verificationCodeService = verificationCodeService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String smsCode = parameters.get("smsCode");
        String mobilePhone = parameters.get("mobilePhone");
        boolean smsCodeValid = verificationCodeService.checkSmsCode(mobilePhone, smsCode);
        if (!smsCodeValid) {
            throw new BusinessException("验证码不正确或已过期");
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
