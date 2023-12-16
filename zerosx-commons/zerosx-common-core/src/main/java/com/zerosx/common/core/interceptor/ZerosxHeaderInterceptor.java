package com.zerosx.common.core.interceptor;

import com.zerosx.common.base.constants.HeadersConstants;
import com.zerosx.common.core.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ZerosxHeaderInterceptor implements AsyncHandlerInterceptor {

    private static final String ERROR = "/error";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (ERROR.equals(requestURI)) {
            return true;
        }
        String clientId = ServletUtils.getHeader(request, HeadersConstants.CLIENT_ID);
        ZerosSecurityContextHolder.set(HeadersConstants.CLIENT_ID, clientId);
        String token = ServletUtils.getToken(request, HeadersConstants.TOKEN);
        ZerosSecurityContextHolder.set(HeadersConstants.TOKEN, token);
        String loginUserName = ServletUtils.getHeader(request, HeadersConstants.USERNAME);
        if (StringUtils.isBlank(loginUserName)) {
            loginUserName = ZerosSecurityContextHolder.getUserName();
        }
        ZerosSecurityContextHolder.setUserName(loginUserName);
        String userId = ServletUtils.getHeader(request, HeadersConstants.USERID);
        ZerosSecurityContextHolder.setUserId(userId);
        String userType = ServletUtils.getHeader(request, HeadersConstants.USERTYPE);
        ZerosSecurityContextHolder.setUserType(userType);
        String operatorIds = ServletUtils.getHeader(request, HeadersConstants.OPERATOR_ID);
        log.debug("【{}】登录用户名:{} 租户:{} 请求:【{}】", clientId, loginUserName, operatorIds, requestURI);
        if (StringUtils.isNotBlank(operatorIds)) {
            ZerosSecurityContextHolder.setOperatorIds(operatorIds);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ZerosSecurityContextHolder.remove();
        //log.debug("清除ZerosSecurityContextHolder------------");
    }

}
