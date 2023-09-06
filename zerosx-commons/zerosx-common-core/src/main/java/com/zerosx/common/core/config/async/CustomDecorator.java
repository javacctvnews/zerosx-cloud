package com.zerosx.common.core.config.async;

import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

@Slf4j
public class CustomDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 主线程
        Map<String, Object> zerosxMap = ZerosSecurityContextHolder.getLocalMap();
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 子线程
        return () -> {
            try {
                // 将变量重新放入到run线程中
                if (MapUtils.isNotEmpty(zerosxMap)) {
                    ZerosSecurityContextHolder.setLocalMap(zerosxMap);
                }
                if (MapUtils.isNotEmpty(mdcMap)) {
                    MDC.setContextMap(mdcMap);
                }
                if (attributes != null) {
                    RequestContextHolder.setRequestAttributes(attributes);
                }
                runnable.run();
            } finally {
                ZerosSecurityContextHolder.remove();
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
