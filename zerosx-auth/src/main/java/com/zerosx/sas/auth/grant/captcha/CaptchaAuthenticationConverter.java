package com.zerosx.sas.auth.grant.captcha;

import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.token.CaptchaAuthenticationToken;
import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationConverter;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.utils.SasAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * CaptchaAuthenticationConverter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:10
 **/
@Slf4j
public class CaptchaAuthenticationConverter extends CustomAbstractAuthenticationConverter<CaptchaAuthenticationToken> {

    private final RedissonOpService redissonOpService;

    public CaptchaAuthenticationConverter(RedissonOpService redissonOpService) {
        this.redissonOpService = redissonOpService;
    }

    protected boolean checkGrantType(String grantType) {
        return CustomAuthorizationGrantType.CAPTCHA_PWD.getValue().equals(grantType);
    }

    /**
     * 校验扩展参数 密码模式密码必须不为空
     *
     * @param parameters 参数列表
     */
    public void checkRequestParams(MultiValueMap<String, String> parameters) {
        // 用户名不能为空
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME);
        }
        // 密码不能为空
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD);
        }
        // 验证码不能为空
        String captchaCode = parameters.getFirst(OAuth2ParameterNames.CODE);
        if (!StringUtils.hasText(captchaCode) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CODE);
        }
        // 验证码序列Id 不能为空
        String captchaUUID = parameters.getFirst(CustomOAuth2ParameterNames.CAPTCHA_UUID);
        if (!StringUtils.hasText(captchaUUID) || parameters.get(CustomOAuth2ParameterNames.CAPTCHA_UUID).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CustomOAuth2ParameterNames.CAPTCHA_UUID);
        }
        // 检查验证码是否正确
        String key = ZCache.CAPTCHA.key(captchaUUID);
        String cacheCaptchaCode = redissonOpService.get(key);
        if (!StringUtils.hasText(cacheCaptchaCode)) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "验证码已失效");
        }
        if (!cacheCaptchaCode.equals(captchaCode)) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "验证码错误");
        }
    }

    protected CaptchaAuthenticationToken buildAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new CaptchaAuthenticationToken(CustomAuthorizationGrantType.CAPTCHA_PWD, clientPrincipal, requestedScopes, additionalParameters);
    }

}
