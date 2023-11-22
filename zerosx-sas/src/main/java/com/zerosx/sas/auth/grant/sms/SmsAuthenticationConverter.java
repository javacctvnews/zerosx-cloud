package com.zerosx.sas.auth.grant.sms;

import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.sas.token.SmsAuthenticationToken;
import com.zerosx.sas.auth.grant.CustomAbstractAuthenticationConverter;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.utils.SasAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;

/**
 * SmsAuthenticationConverter
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-23 14:10
 **/
@Slf4j
public class SmsAuthenticationConverter extends CustomAbstractAuthenticationConverter<SmsAuthenticationToken> {

    private final RedissonOpService redissonOpService;

    public SmsAuthenticationConverter(RedissonOpService redissonOpService) {
        this.redissonOpService = redissonOpService;
    }

    protected boolean checkGrantType(String grantType) {
        return CustomAuthorizationGrantType.SMS.getValue().equals(grantType);
    }

    public void checkRequestParams(MultiValueMap<String, String> parameters) {
        // 手机号码 必填
        String phone = parameters.getFirst(CustomOAuth2ParameterNames.PHONE);
        if (StringUtils.isBlank(phone) || parameters.get(CustomOAuth2ParameterNames.PHONE).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CustomOAuth2ParameterNames.PHONE);
        }
        // 短信验证码 必填
        String smsCode = parameters.getFirst(CustomOAuth2ParameterNames.SMS_CODE);
        if (StringUtils.isBlank(smsCode) || parameters.get(CustomOAuth2ParameterNames.SMS_CODE).size() != 1) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CustomOAuth2ParameterNames.SMS_CODE);
        }
        //验证码是否有效
        String cacheCode = redissonOpService.get(RedisKeyNameEnum.key(RedisKeyNameEnum.SMS_CODE, phone));
        if (StringUtils.isBlank(cacheCode)) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "验证码不存在或已过期");
        }
        if (!smsCode.equals(cacheCode.toLowerCase())) {
            SasAuthUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, "验证码不正确");
        }
    }

    protected SmsAuthenticationToken buildAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new SmsAuthenticationToken(CustomAuthorizationGrantType.SMS, clientPrincipal, requestedScopes, additionalParameters, clientPrincipal);
    }

}
