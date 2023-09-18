package com.zerosx.auth.config.auth;

import com.zerosx.auth.service.IOauthTokenRecordService;
import com.zerosx.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class WebResponseExceptionTranslatorConfig {

    @Autowired
    private TokenStore tokenStore;

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            private static final String BAD_MSG = "坏的凭证";
            private static final String BAD_MSG_EN = "Bad credentials";

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                log.error(e.getMessage(), e);
                OAuth2Exception oAuth2Exception = null;
                String errorMsg = "";
                int status = 401;
                if (e.getMessage() != null && (BAD_MSG.equals(e.getMessage()) || BAD_MSG_EN.equals(e.getMessage()))) {
                    oAuth2Exception = new InvalidGrantException("客户端密码错误", e);
                }
                ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
                IOauthTokenRecordService tokenRecordService = SpringUtils.getBean(IOauthTokenRecordService.class);
                tokenRecordService.updateOauthResult("", errorMsg);
                ResponseEntity.status(oAuth2Exception.getHttpErrorCode());
                response.getBody().addAdditionalInformation("code", String.valueOf(status));
                response.getBody().addAdditionalInformation("msg", errorMsg);
                return response;
            }
        };
    }

    /**
     * 登陆成功
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            Assert.notNull(tokenStore, "tokenStore must be set");
            String token = request.getParameter("token");
//            if (StringUtils.isEmpty(token)) {
//                token = AuthUtils.extractToken(request);
//            }
            if(StringUtils.isNotBlank(token)){
                OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
                OAuth2RefreshToken refreshToken;
                if (existingAccessToken != null) {
                    if (existingAccessToken.getRefreshToken() != null) {
                        log.debug("remove refreshToken:{}", existingAccessToken.getRefreshToken());
                        refreshToken = existingAccessToken.getRefreshToken();
                        tokenStore.removeRefreshToken(refreshToken);
                    }
                    log.debug("remove existingAccessToken:{}", existingAccessToken);
                    tokenStore.removeAccessToken(existingAccessToken);
                }
            }
        };
    }


}
