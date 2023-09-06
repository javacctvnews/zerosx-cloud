package com.zerosx.common.loadbalancer.interceptor;

import com.zerosx.common.base.constants.HeadersConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * feign传递自定义请求头
 */
@Slf4j
public class FeignHttpInterceptor implements RequestInterceptor {

    private static final Set<String> feignHeaders = new HashSet<>();

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return;
        }
        if (feignHeaders.isEmpty()) {
            initHeaders();
            if (feignHeaders.isEmpty()) {
                return;
            }
        }
        HttpServletRequest request = attrs.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                boolean match = feignHeaders.stream().anyMatch(item -> item.equalsIgnoreCase(name));
                if (match) {
                    requestTemplate.header(name, value);
                    //log.debug("传递Feign请求头:{} 值：{}", name, value);
                }
            }
        }
    }

    private void initHeaders() {
        feignHeaders.add(HeadersConstants.USERNAME);
        feignHeaders.add(HeadersConstants.USERID);
        feignHeaders.add(HeadersConstants.USERTYPE);
        feignHeaders.add(HeadersConstants.CLIENT_ID);
        feignHeaders.add(HeadersConstants.OPERATOR_ID);
        feignHeaders.add(HeadersConstants.TOKEN);
        //feignHeaders.add("log-traceId-header");
        //feignHeaders.add("log-spanId-header");
        log.debug("初始化Feign请求头......");
    }

}
