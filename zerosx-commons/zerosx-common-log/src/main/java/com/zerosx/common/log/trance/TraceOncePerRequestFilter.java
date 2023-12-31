package com.zerosx.common.log.trance;

import com.zerosx.common.core.utils.MDCTraceUtils;
import com.zerosx.common.log.properties.CustomLogProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * web过滤器，生成日志链路追踪id，并赋值MDC
 */
@Slf4j
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@Order(value = MDCTraceUtils.FILTER_ORDER)
public class TraceOncePerRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomLogProperties customLogProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !customLogProperties.getEnable();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = request.getHeader(MDCTraceUtils.TRACE_ID_HEADER);
            String spanId = request.getHeader(MDCTraceUtils.SPAN_ID_HEADER);
            if (StringUtils.isBlank(traceId)) {
                MDCTraceUtils.addTrace();
            } else {
                MDCTraceUtils.putTrace(traceId, spanId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDCTraceUtils.removeTrace();
        }
    }
}
