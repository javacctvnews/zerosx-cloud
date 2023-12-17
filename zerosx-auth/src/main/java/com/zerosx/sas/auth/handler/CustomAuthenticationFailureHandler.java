package com.zerosx.sas.auth.handler;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.sas.auth.grant.CustomOAuth2ParameterNames;
import com.zerosx.sas.service.IOauthTokenRecordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * CustomAuthenticationFailureHandler
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-24 10:26
 **/
@Slf4j
public class CustomAuthenticationFailureHandler extends BaseAuthenticationHandler implements AuthenticationFailureHandler {

    private final IOauthTokenRecordService oauthTokenRecordService;

    public CustomAuthenticationFailureHandler(IOauthTokenRecordService oauthTokenRecordService) {
        this.oauthTokenRecordService = oauthTokenRecordService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String typeName = exception.getClass().getName();
        log.error("执行CustomAuthenticationFailureHandler，异常类型:{}", typeName, exception);
        String error;
        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            OAuth2Error oAuth2Error = oAuth2AuthenticationException.getError();
            error = oAuth2Error.toString();
            if (error.contains(CustomOAuth2ParameterNames.CLIENT_SECRET_EXPIRES_AT)) {
                error = "客户端密码有效日期已过期";
            }
        } else if (exception instanceof BadCredentialsException badCredentialsException) {
            error = badCredentialsException.toString();
        } else {
            error = exception.getMessage();
        }
        saveRecord(oauthTokenRecordService, request, "", error);
        // 错误日志
        ResultVO<?> fail = ResultVOUtil.error(HttpStatus.UNAUTHORIZED.value(), error);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JacksonUtil.toJSONString(fail));
        response.getWriter().flush();
    }
}
