//package com.zerosx.sentinel.config;
//
//import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
//import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
//import com.zerosx.base.utils.ResultVOUtil;
//import com.zerosx.base.vo.ResultVO;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class SentinelAutoConfig {
//    /**
//     * 限流、熔断统一处理类
//     */
//    @Configuration
//    @ConditionalOnClass(HttpServletRequest.class)
//    public static class WebmvcHandler {
//        @Bean
//        public BlockExceptionHandler webmvcBlockExceptionHandler() {
//            return (request, response, e) -> {
//                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//                ResultVO<?> result = new ResultVO<>(String.valueOf(response.getStatus()), e.getMessage());
//                response.getWriter().print(result);
//            };
//        }
//    }
//
//    /**
//     * 限流、熔断统一处理类
//     */
//    @Configuration
//    @ConditionalOnClass(org.springframework.web.reactive.function.server.ServerResponse.class)
//    public static class WebfluxHandler {
//        @Bean
//        public BlockRequestHandler webfluxBlockExceptionHandler() {
//            return (exchange, t) ->
//                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(BodyInserters.fromValue(ResultVOUtil.error(String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()), t.getMessage())));
//        }
//    }
//}
