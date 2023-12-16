package com.zerosx.gateway.handler;

import com.zerosx.common.base.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * 在SpringCloud gateway中默认使用 DefaultErrorWebExceptionHandler来处理异常
 * 将异常封装成 ResultVO 格式
 */
@Slf4j
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    private static final String STATUS = "status";

    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        int httpStatus = super.getHttpStatus(errorAttributes);
        errorAttributes.remove(STATUS);
        return httpStatus;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);
        //log.error(error.getMessage(), error);
        String message = error.getMessage();
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put(STATUS, ResultEnum.FAIL.getCode());
        if (error instanceof NotFoundException) {
            NotFoundException e = (NotFoundException) error;
            HttpStatus httpStatus = e.getStatus();
            if (404 == httpStatus.value()) {
                message = "接口访问404,请查看路由信息是否正确";
                errorAttributes.put(STATUS, HttpStatus.NOT_FOUND.value());
            } else if (503 == httpStatus.value()) {
                errorAttributes.put(STATUS, HttpStatus.SERVICE_UNAVAILABLE.value());
                Route route = request.exchange().getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                if (route != null) {
                    String id = route.getUri().getHost();
                    message = "服务不可用：" + id;
                } else {
                    message = "服务不可用：" + request.uri();
                }
            }
        } else if (error instanceof ResponseStatusException) {
            if (404 == ((ResponseStatusException) error).getStatus().value()) {
                message = "接口访问404,请查看路由信息是否正确或者当前服务是否正常";
            } else {
                message = "其他异常";
            }
            errorAttributes.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (error instanceof InvalidTokenException) {
            errorAttributes.put(STATUS, ResultEnum.UNAUTHORIZED.getCode());
            message = ResultEnum.UNAUTHORIZED.getMessage();
        } else {
            errorAttributes.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        errorAttributes.put("code", errorAttributes.get(STATUS));
        errorAttributes.put("msg", message);
        errorAttributes.put("data", StringUtils.EMPTY);
        return errorAttributes;
    }
}
