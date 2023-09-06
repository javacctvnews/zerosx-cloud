package com.zerosx.common.core.config;

import com.zerosx.common.base.constants.SecurityConstants;
import com.zerosx.common.core.interceptor.ZerosxHeaderInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 默认SpringMVC拦截器
 */
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ZerosxHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(SecurityConstants.ENDPOINTS)
                .order(-10);
    }

}
