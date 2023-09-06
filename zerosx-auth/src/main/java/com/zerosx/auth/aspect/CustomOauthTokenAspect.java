package com.zerosx.auth.aspect;

import com.zerosx.auth.service.IOauthTokenRecordService;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class CustomOauthTokenAspect {

    @Autowired
    private IOauthTokenRecordService oauthTokenRecordService;

    /**
     * 将[/oauth/token]的结果输出ResultVO的格式
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object pointAround(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        log.debug("请求授权参数:{}", args);
        ZerosSecurityContextHolder.set(CommonConstants.OAUTH_REQUEST_ID, IdGenerator.getIdStr());
        Principal principal = (Principal) args[0];
        oauthTokenRecordService.saveBeforeOauthRecord(getClientId(principal), (Map<String, String>) args[1]);
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            oauthTokenRecordService.updateOauthResult("", throwable.getMessage());
            return ResponseEntity.internalServerError().body(ResultVOUtil.error(throwable.getMessage()));
        }
        ResponseEntity<OAuth2AccessToken> res = (ResponseEntity<OAuth2AccessToken>) proceed;
        OAuth2AccessToken body = res.getBody();
        oauthTokenRecordService.updateOauthResult(body == null ? "" : body.getValue(), ResultEnum.SUCCESS.getMessage());
        if (body == null) {
            return ResponseEntity.internalServerError().body(ResultVOUtil.error("颁发Token为空，授权失败"));
        }
        log.debug("授权结果:{}", body);
        ResultVO<OAuth2AccessToken> succRes = ResultVOUtil.success(body);
        return ResponseEntity.ok().body(succRes);
    }

    private String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        }
        String clientId = client.getName();
        if (client instanceof OAuth2Authentication) {
            clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
        }
        return clientId;
    }

}
