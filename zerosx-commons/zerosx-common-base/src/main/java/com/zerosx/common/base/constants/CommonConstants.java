package com.zerosx.common.base.constants;

/**
 * 全局公共常量
 */
public interface CommonConstants {
    /**
     * 系统标识
     */
    String ZEROSX = "zerosx-cloud";

    /**
     * token请求头名称
     */
    String TOKEN_HEADER = "Authorization";

    String BEARER_TYPE = "Bearer";

    String AT = "@";

    int ROCKET_REPEAT = 100;

    /**
     * 授权的账户类型
     */
    String AUTH_ACCOUNT_TYPE = "auth_account_type";

    /**
     * 租户ID
     */
    String OPERATOR_ID = "operator_id";

    /**
     * User-Agent
     */
    String UNKNOWN_STR = "unknown";

    /**
     * www主域
     */
    String WWW = "www.";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * com.zerosx.common.api.anno.Trans.type()
     */
    String TRANS_ENUMS = "enums";
    String TRANS_DICT = "dict";
    String TRANS_OSS = "oss";
    String TRANS_REGION = "region";
    String TRANS_OPERATOR_ID = "operatorId";

    /**
     * 授权记录requestId
     */
    String OAUTH_REQUEST_ID = "oauth_request_id";

    /**
     * User-Agent
     */
    String USER_AGENT = "User-Agent";

    /**
     * 包路径
     */
    String BASE_PACKAGE = "com.zerosx";
}
