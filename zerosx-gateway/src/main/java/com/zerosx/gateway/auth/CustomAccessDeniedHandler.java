package com.zerosx.gateway.auth;

import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.gateway.utils.WebFluxRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 403拒绝访问异常处理
 */
@Slf4j
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        HttpStatus statusCode = exchange.getResponse().getStatusCode();
        log.debug("【权限不足】的URL请求:{} HttpStatus：{}", exchange.getRequest().getPath(), statusCode);
        //log.error(e.getMessage(), e);
        return WebFluxRespUtils.responseWrite(exchange, ResultVOUtil.error(ResultEnum.FORBIDDEN));
    }

}
