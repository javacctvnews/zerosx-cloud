package com.zerosx.sas.auth.handler;

import com.zerosx.sas.auth.converter.CustomOAuth2AccessTokenResponseHttpMessageConverter;
import com.zerosx.sas.service.IOauthTokenRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
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
public class CustomAuthenticationSuccessHandler extends BaseAuthenticationHandler implements AuthenticationSuccessHandler {

    private final IOauthTokenRecordService oauthTokenRecordService;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new CustomOAuth2AccessTokenResponseHttpMessageConverter();

    public CustomAuthenticationSuccessHandler(IOauthTokenRecordService oauthTokenRecordService) {
        this.oauthTokenRecordService = oauthTokenRecordService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 记录登录日志
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        String tokenValue = accessTokenAuthentication.getAccessToken().getTokenValue();
        saveRecord(oauthTokenRecordService, request, tokenValue, "");
        // 输出token
        sendAccessTokenResponse(request, response, authentication);
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
