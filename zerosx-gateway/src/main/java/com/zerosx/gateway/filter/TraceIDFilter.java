package com.zerosx.gateway.filter;

import com.zerosx.common.core.utils.MDCTraceUtils;
import com.zerosx.common.log.properties.CustomLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 生成日志链路追踪id，并传入header中
 */
@Slf4j
public class TraceIDFilter implements WebFilter, Ordered {

    private final CustomLogProperties customLogProperties;

    public TraceIDFilter(CustomLogProperties customLogProperties) {
        this.customLogProperties = customLogProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (customLogProperties.getEnable()) {
            //链路追踪id
            MDCTraceUtils.addTrace();
            log.debug("执行TraceIDFilter:{}", exchange.getRequest().getPath());
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .headers(h -> {
                        h.add(MDCTraceUtils.TRACE_ID_HEADER, MDCTraceUtils.getTraceId());
                        h.add(MDCTraceUtils.SPAN_ID_HEADER, MDCTraceUtils.getNextSpanId());
                    })
                    .build();
            ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
