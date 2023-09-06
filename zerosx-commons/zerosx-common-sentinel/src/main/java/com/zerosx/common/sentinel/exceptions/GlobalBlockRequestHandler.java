//package com.zerosx.sentinel.exceptions;
//
//import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
//import com.zerosx.common.base.vo.ResultVO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class GlobalBlockRequestHandler implements BlockRequestHandler {
//
//    @Override
//    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
//        log.error(throwable.getMessage(), throwable);
//        ResultVO<?> result = new ResultVO<>(String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
//        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
//    }
//
//}
