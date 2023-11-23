package com.zerosx.sas.auth.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.utils.IpUtils;
import com.zerosx.sas.auth.converter.CustomOAuth2AccessTokenResponseHttpMessageConverter;
import com.zerosx.sas.entity.OauthTokenRecord;
import com.zerosx.sas.service.IOauthTokenRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * CustomAuthenticationSuccessHandler
 * <p>
 * 登录成功处理类
 *
 * @author javacctvnews
 * @date 2023-10-24 10:26
 **/
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final IOauthTokenRecordService oauthTokenRecordService;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new CustomOAuth2AccessTokenResponseHttpMessageConverter();

    public CustomAuthenticationSuccessHandler(IOauthTokenRecordService oauthTokenRecordService) {
        this.oauthTokenRecordService = oauthTokenRecordService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        // 输出token
        sendAccessTokenResponse(request, response, authentication);
        // 记录登录日志
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        //Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();
        OauthTokenRecord otr = new OauthTokenRecord();
        otr.setClientId(accessTokenAuthentication.getRegisteredClient().getClientId());
        otr.setApplyOauthTime(new Date());
        otr.setUsername(ZerosSecurityContextHolder.get(OAuth2ParameterNames.USERNAME));
        if(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantType)){
            otr.setUsername(otr.getClientId());
        }
        otr.setRequestId(IdGenerator.getIdStr());
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(CommonConstants.USER_AGENT));
        otr.setBrowserType(userAgent.getBrowser().getName() + "/" + userAgent.getVersion());
        otr.setOsType(userAgent.getPlatform() + "/" + userAgent.getOsVersion());
        otr.setSourceIp(IpUtils.getRemoteAddr(request));
        otr.setSourceLocation(IpUtils.getIpLocation(otr.getSourceIp()));
        //String grantType = StringUtils.isBlank((String) additionalParameters.get(OAuth2ParameterNames.GRANT_TYPE)) ? "" : (String) additionalParameters.get(OAuth2ParameterNames.GRANT_TYPE);
        otr.setGrantType(grantType);
        otr.setOauthResult(0);
        otr.setOauthMsg(ResultEnum.SUCCESS.getMessage());
        otr.setTokenValue(accessTokenAuthentication.getAccessToken().getTokenValue());
        otr.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        oauthTokenRecordService.save(otr);
    }

    /**
     * 源码：OAuth2TokenEndpointFilter.sendAccessTokenResponse 方法
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse
                .withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

}
