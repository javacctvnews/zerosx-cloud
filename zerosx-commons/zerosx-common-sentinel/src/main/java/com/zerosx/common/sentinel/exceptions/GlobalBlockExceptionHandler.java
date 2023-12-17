//package com.zerosx.sentinel.exceptions;
//
//import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
//import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
//import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
//import com.zerosx.common.base.vo.ResultVO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//@Slf4j
//public class GlobalBlockExceptionHandler implements BlockExceptionHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException blockException) throws Exception {
//        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//        log.error(blockException.getMessage(), blockException);
//        ResultVO<?> result = new ResultVO<>(String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
//        result.setCode(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
//        if (blockException instanceof FlowException) {
//            result.setMsg("已限流");
//        } else if (blockException instanceof ParamFlowException) {
//            result.setMsg("热点参数已限流");
//        } else if (blockException instanceof DegradeException) {
//            result.setMsg("已降级");
//        } else if (blockException instanceof SystemBlockException) {
//            result.setMsg("已触发系统保护规则");
//        } else if (blockException instanceof AuthorityException) {
//            result.setMsg("授权未通过");
//        }
//        response.getWriter().print(result);
//    }
//
//}
