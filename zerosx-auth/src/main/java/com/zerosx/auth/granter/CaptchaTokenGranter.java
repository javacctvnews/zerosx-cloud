package com.zerosx.auth.granter;

import com.zerosx.auth.service.IVerificationCodeService;
import com.zerosx.common.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author javacctvnews
 * @description 用户名密码+图形验证码授权模式
 * @date Created in 2023/8/10 17:34
 * @modify by
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "captcha";

    private final AuthenticationManager authenticationManager;

    private final IVerificationCodeService verificationCodeService;

    public CaptchaTokenGranter(AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory,
                                  AuthenticationManager authenticationManager,
                                  IVerificationCodeService verificationCodeService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.verificationCodeService = verificationCodeService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        // 验证码校验逻辑
        String validateCode = parameters.get("code");
        String uuid = parameters.get("uuid");
        if(StringUtils.isBlank(validateCode)){
            throw new BusinessException("验证码不能为空");
        }
        //校验验证码
        verificationCodeService.validateImgCode(uuid, validateCode);
        //用户名、密码
        String username = parameters.get("username");
        String password = parameters.get("password");
        // 移除后续无用参数
        parameters.remove("password");
        parameters.remove("code");
        parameters.remove("uuid");
        // 和密码模式一样的逻辑
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ex) {
            throw new InvalidGrantException(ex.getMessage());
        }
        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
    }
}
