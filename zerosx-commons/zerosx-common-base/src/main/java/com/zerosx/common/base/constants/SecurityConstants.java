package com.zerosx.common.base.constants;

public interface SecurityConstants {

    /**
     * 默认token过期时间(2小时)
     */
    Integer ACCESS_TOKEN_VALIDITY_SECONDS = 2 * 60 * 60;

    /**
     * 续签token, token剩余时间/有效时间 <= RENEW_TOKEN_PERCENT
     */
    double RENEW_TOKEN_PERCENT = 0.2;

    /**
     * 无需认证的一些端点URL
     */
    String[] ENDPOINTS = {
            "/oauth/token",
            "/actuator/**",
            "/*/v2/api-docs",
            "/doc.html",
            "/webjars/**",
            "/druid/**",
            "/favicon.ico",
            "/*/v3/api-docs/default",
            "/monitor/**"
    };

    /**
     * 客户端 认证模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 授权用户类型字段
     */
    String USER_AUTH_TYPE = "user_auth_type";
    /**
     * security上下文-用户信息
     */
    String SECURITY_CONTEXT = "LoginUserTenantsBO";
}
