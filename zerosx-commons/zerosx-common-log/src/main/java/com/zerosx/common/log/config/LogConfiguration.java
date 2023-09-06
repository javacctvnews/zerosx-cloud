package com.zerosx.common.log.config;

import com.zerosx.common.core.utils.MDCTraceUtils;
import com.zerosx.common.log.properties.TraceProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 日志自动配置
 *
 * @author javacctvnews
 */
@AutoConfiguration
@EnableConfigurationProperties({TraceProperties.class})
public class LogConfiguration {

    @Autowired
    private TraceProperties traceProperties;

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {
            if (traceProperties.getEnable()) {
                //传递日志traceId spanId
                String traceId = MDCTraceUtils.getTraceId();
                if (StringUtils.isNotBlank(traceId)) {
                    template.header(MDCTraceUtils.TRACE_ID_HEADER, traceId);
                    template.header(MDCTraceUtils.SPAN_ID_HEADER, MDCTraceUtils.getNextSpanId());
                }
            }
        };
    }


}
