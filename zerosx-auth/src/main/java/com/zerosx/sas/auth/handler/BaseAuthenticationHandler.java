package com.zerosx.sas.auth.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.utils.IpUtils;
import com.zerosx.sas.auth.grant.CustomAuthorizationGrantType;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.entity.OauthTokenRecord;
import com.zerosx.sas.service.IOauthTokenRecordService;
import com.zerosx.sas.utils.SasAuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

import java.util.Date;

/**
 * BaseAuthenticationHandler
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-06 18:40
 **/
@Slf4j
public class BaseAuthenticationHandler {

    /**
     * 记录登录日志
     *
     * @param oauthTokenRecordService IOauthTokenRecordService
     * @param request                 HttpServletRequest
     * @param token                   Token
     * @param error                   错误信息
     */
    protected void saveRecord(IOauthTokenRecordService oauthTokenRecordService, HttpServletRequest request, String token, String error) {
        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);
        OauthTokenRecord otr = new OauthTokenRecord();
        otr.setGrantType(parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE));
        otr.setClientId(parameters.getFirst(OAuth2ParameterNames.CLIENT_ID));
        otr.setApplyOauthTime(new Date());
        otr.setUsername(ZerosSecurityContextHolder.get(OAuth2ParameterNames.USERNAME));
        if (CustomAuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(otr.getGrantType())) {
            otr.setUsername(otr.getClientId());
        } else if (CustomAuthorizationGrantType.SMS.getValue().equals(otr.getGrantType())) {
            otr.setUsername(parameters.getFirst(CustomOAuth2ParameterNames.PHONE));
        }
        otr.setRequestId(IdGenerator.getIdStr());
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(CommonConstants.USER_AGENT));
        otr.setBrowserType(userAgent.getBrowser().getName() + "/" + userAgent.getVersion());
        otr.setOsType(userAgent.getPlatform() + "/" + userAgent.getOsVersion());
        otr.setSourceIp(IpUtils.getRemoteAddr(request));
        otr.setSourceLocation(IpUtils.getIpLocation(otr.getSourceIp()));
        otr.setTokenValue(token);
        otr.setOauthResult(0);
        otr.setOauthMsg(ResultEnum.SUCCESS.getMessage());
        if (StringUtils.isNotBlank(error)) {
            otr.setOauthResult(1);
            otr.setOauthMsg(error);
        }
        otr.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        try {
            oauthTokenRecordService.save(otr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
