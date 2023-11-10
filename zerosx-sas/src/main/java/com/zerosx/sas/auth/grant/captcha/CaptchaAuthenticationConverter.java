package com.zerosx.sas.auth.grant.captcha;

import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.service.IVerificationCodeService;
import com.zerosx.sas.utils.SasAuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * CaptchaAuthenticationConverter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:10
 **/
@Slf4j
public class CaptchaAuthenticationConverter implements AuthenticationConverter {

    private final RedissonOpService redissonOpService;
    private final IVerificationCodeService verificationCodeService;

    public CaptchaAuthenticationConverter(RedissonOpService redissonOpService, IVerificationCodeService verificationCodeService) {
        this.redissonOpService = redissonOpService;
        this.verificationCodeService = verificationCodeService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        if (!checkGrantType(request)) {
            //SasAuthUtils.throwError(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, OAuth2ParameterNames.GRANT_TYPE);
            //不匹配时必须返回null
            return null;
        }

        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);

        // scope (OPTIONAL)
        Set<String> scopes = null;
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE);
        }
        if (StringUtils.hasText(scope)) {
            scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        checkRequestParam(request);

        // 这里目前是客户端认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT);
        }

        // 提取附加参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value.get(0));
            }
        });
        return buildAuthenticationToken(clientPrincipal, scopes, additionalParameters);
    }

    protected boolean checkGrantType(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        return CustomAuthorizationGrantType.CAPTCHA_PWD.getValue().equals(grantType);
    }

    /**
     * 校验扩展参数 密码模式密码必须不为空
     *
     * @param request 参数列表
     */
    public void checkRequestParam(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);
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
        //验证码序列Id 不能为空
        String captchaUUID = parameters.getFirst(CustomOAuth2ParameterNames.CAPTCHA_UUID);
        if (!StringUtils.hasText(captchaUUID) || parameters.get(CustomOAuth2ParameterNames.CAPTCHA_UUID).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CustomOAuth2ParameterNames.CAPTCHA_UUID);
        }

        String cacheCaptchaCode = redissonOpService.get(RedisKeyNameEnum.key(RedisKeyNameEnum.IMAGE_CODE, captchaUUID));
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
