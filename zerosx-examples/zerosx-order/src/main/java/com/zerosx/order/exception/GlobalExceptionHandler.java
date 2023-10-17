package com.zerosx.order.exception;

import com.zerosx.common.core.exception.DefaultExceptionAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order()
public class GlobalExceptionHandler extends DefaultExceptionAdvice {


}
