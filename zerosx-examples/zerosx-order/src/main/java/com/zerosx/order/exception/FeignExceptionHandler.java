//package com.zerosx.order.exception;
//
//import com.zerosx.api.examples.IOrderControllerApi;
//import com.zerosx.common.base.exception.BusinessException;
//import com.zerosx.common.base.vo.ResultVO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@Slf4j
//@RestControllerAdvice(basePackageClasses = {IOrderControllerApi.class})
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class FeignExceptionHandler {
//
//    /**
//     * 所有异常统一处理
//     * 返回状态码:500
//     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = Exception.class)
//    public ResultVO<?> handleException(Exception e) throws Exception {
//        throw e;
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = BusinessException.class)
//    public ResultVO<?> businessException(BusinessException e) {
//        throw e;
//    }
//}
