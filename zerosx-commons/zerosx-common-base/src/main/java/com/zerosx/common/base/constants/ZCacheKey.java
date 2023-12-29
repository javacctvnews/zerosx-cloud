package com.zerosx.common.base.constants;


public interface ZCacheKey {

    //运营商
    String OPERATOR = ServiceIdConstants.SYSTEM + ":" + "operator";
    //图形验证码
    String CAPTCHA = ServiceIdConstants.RESOURCE + ":" + "captcha";
    //防重令牌
    String IDEMPOTENT_TOKEN = ServiceIdConstants.RESOURCE + ":" + "idempotent_token";
    //部门
    String DEPT = ServiceIdConstants.SYSTEM + ":" + "dept";
    //短信验证码
    String SMS_CODE = ServiceIdConstants.RESOURCE + ":" + "sms_code";
    //当前登录用户基础信息（角色集合和租户信息）
    String CURRENT_USER = ServiceIdConstants.SYSTEM + ":" + "current_user_data";
    //角色对应的权限集合
    String ROLE_PERMISSIONS = ServiceIdConstants.SYSTEM + ":" + "role_permissions";
    //省市区 json字符串结构
    String REGION = ServiceIdConstants.RESOURCE + ":" + "region";
    //省市区 hash结构
    String REGION_HASH = ServiceIdConstants.RESOURCE + ":" + "region_hash";
    //oss文件的访问URL string结构
    String OSS_FILE_URL = ServiceIdConstants.RESOURCE + ":" + "oss_file_url";
    //数据字典 hash结构
    String DICT_DATA = ServiceIdConstants.RESOURCE + ":" + "dict_data";
    //OAuth客户端配置
    String OAUTH_CLIENT_DETAILS = ServiceIdConstants.AUTH + ":" + "oauth_client_details";
    //操作日志
    String SYS_OP_LOG = ServiceIdConstants.SYSTEM + ":" + "sys_op_log";

    /* sas认证 start */
    //access_token
    String SAS_ACCESS_TOKEN = ServiceIdConstants.AUTH + ":" + "authorization:access_token";
    //refresh_token
    String SAS_REFRESH_TOKEN = ServiceIdConstants.AUTH + ":" + "authorization:refresh_token";
    //token分页
    String SAS_TOKEN_PAGE = ServiceIdConstants.AUTH + ":" + "authorization:page_list";
    /* sas认证 end */

}
