package com.zerosx.common.base.constants;

/**
 * org.springframework.security.oauth2.provider.token.TokenStore的key
 */
public interface TokenStoreConstants {
    /**
     * 存放AccessToken对象
     */
    String ACCESS = "access:";
    /**
     * 缓存的也是AccessToken 对象，是可以根据用户名和client_id来查找当前用户的AccessToken
     */
    String AUTH_TO_ACCESS = "auth_to_access:";
    /**
     * 用来存放用户信息（OAuth2Authentication），有权限信息，用户信息等
     */
    String AUTH = "auth:";

    String REFRESH_AUTH = "refresh_auth:";
    /**
     * 可以根据该值，通过AccessToken找到refreshToken
     */
    String ACCESS_TO_REFRESH = "access_to_refresh:";
    /**
     * 用来存放refreshToken
     */
    String REFRESH = "refresh:";

    String REFRESH_TO_ACCESS = "refresh_to_access:";

    /**
     * 存在内存溢出问题
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     */
    String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    /**
     * 存在内存溢出问题
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     */
    String UNAME_TO_ACCESS = "uname_to_access:";
}
