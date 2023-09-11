package com.zerosx.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 表示通过aop框架暴露该代理对象,AopContext能够访问
 * 使该方法可用
 * com.zerosx.common.base.utils.SpringUtils#getAopProxy(java.lang.Object)
 */
@AutoConfiguration
@EnableAspectJAutoProxy(exposeProxy = true)
public class AspectJAutoProxyConfig {

}
