package com.zerosx.gateway.handler;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.gateway.utils.WebFluxRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用户未登录、登录失败、权限不足时的结果处理
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        HttpStatus statusCode = exchange.getResponse().getStatusCode();
        log.debug("Path：{} HttpStatus：{}", exchange.getRequest().getPath(), statusCode);
        //log.error(e.getMessage(), e);
        return WebFluxRespUtils.responseWrite(exchange, ResultVOUtil.error(ResultEnum.UNAUTHORIZED));
    }
}
