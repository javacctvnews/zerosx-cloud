package com.zerosx.sas.auth.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.utils.IpUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.sas.entity.OauthTokenRecord;
import com.zerosx.sas.service.IOauthTokenRecordService;
import com.zerosx.sas.utils.SasAuthUtils;
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
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * CustomAuthenticationFailureHandler
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-24 10:26
 **/
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

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
        } else if (exception instanceof BadCredentialsException badCredentialsException) {
            error = badCredentialsException.toString();
        } else {
            error = exception.getMessage();
        }
        MultiValueMap<String, String> parameters = SasAuthUtils.getParameters(request);
        // 记录登录日志
        OauthTokenRecord otr = new OauthTokenRecord();
        otr.setClientId("");
        otr.setApplyOauthTime(new Date());
        otr.setUsername(parameters.getFirst(OAuth2ParameterNames.USERNAME));
        otr.setRequestId(IdGenerator.getIdStr());
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(CommonConstants.USER_AGENT));
        otr.setBrowserType(userAgent.getBrowser().getName() + "/" + userAgent.getVersion());
        otr.setOsType(userAgent.getPlatform() + "/" + userAgent.getOsVersion());
        otr.setSourceIp(IpUtils.getRemoteAddr(request));
        otr.setSourceLocation(IpUtils.getIpLocation(otr.getSourceIp()));
        otr.setGrantType(parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE));
        otr.setTokenValue("");
        otr.setOauthResult(1);
        otr.setOauthMsg(error);
        otr.setOperatorId(ZerosSecurityContextHolder.getOperatorIds());
        oauthTokenRecordService.save(otr);
        // 错误日志
        ResultVO<?> fail = ResultVOUtil.error(HttpStatus.UNAUTHORIZED.value(), error);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JacksonUtil.toJSONString(fail));
        response.getWriter().flush();
    }
}
