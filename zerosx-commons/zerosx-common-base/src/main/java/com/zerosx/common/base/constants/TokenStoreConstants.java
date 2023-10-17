package com.zerosx.common.base.constants;

/**
 * org.springframework.security.oauth2.provider.token.TokenStore的key
 */
public interface TokenStoreConstants {

    String PREFIX = "token_store:";

    /**
     * 存放AccessToken对象
     */
    String ACCESS = PREFIX + "access:";
    /**
     * 缓存的也是AccessToken 对象，是可以根据用户名和client_id来查找当前用户的AccessToken
     */
    String AUTH_TO_ACCESS = PREFIX + "auth_to_access:";
    /**
     * 用来存放用户信息（OAuth2Authentication），有权限信息，用户信息等
     */
    String AUTH = PREFIX + "auth:";

    String REFRESH_AUTH = PREFIX + "refresh_auth:";
    /**
     * 可以根据该值，通过AccessToken找到refreshToken
     */
    String ACCESS_TO_REFRESH = PREFIX + "access_to_refresh:";
    /**
     * 用来存放refreshToken
     */
    String REFRESH = PREFIX + "refresh:";

    String REFRESH_TO_ACCESS = PREFIX + "refresh_to_access:";

    /**
     * 存在内存溢出问题
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     */
    String CLIENT_ID_TO_ACCESS = PREFIX + "client_id_to_access:";
    /**
     * 存在内存溢出问题
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     */
    String UNAME_TO_ACCESS = PREFIX + "uname_to_access:";
}
