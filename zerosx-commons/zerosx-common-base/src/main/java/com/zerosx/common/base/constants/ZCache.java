package com.zerosx.common.base.constants;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * ZCache
 * <p> redis的key定义
 *
 * @author: javacctvnews
 * @create: 2023-12-08 16:44
 **/

public final class ZCache implements Serializable {

    private static final long serialVersionUID = 2320358632082520428L;

    //图形验证码默认过期时间，单位：秒
    public static final Long CAPTCHA_EXPIRE = 300L;
    //图形验证码
    public static final ZCache CAPTCHA = new ZCache(ServiceIdConstants.RESOURCE, "captcha");

    //运营商
    public static final ZCache OPERATOR = new ZCache(ServiceIdConstants.SYSTEM, "operator");
    //部门
    public static final ZCache DEPT = new ZCache(ServiceIdConstants.SYSTEM, "dept");
    //短信验证码
    public static final ZCache SMS_CODE = new ZCache(ServiceIdConstants.RESOURCE, "sms_code");
    //当前登录用户基础信息（角色集合和租户信息）
    public static final ZCache CURRENT_USER = new ZCache(ServiceIdConstants.SYSTEM, "current_user_data");
    //角色对应的权限集合
    public static final ZCache ROLE_PERMISSIONS = new ZCache(ServiceIdConstants.SYSTEM, "role_permissions");
    //省市区 json字符串结构
    public static final ZCache REGION = new ZCache(ServiceIdConstants.RESOURCE, "region");
    //省市区 hash结构
    public static final ZCache REGION_HASH = new ZCache(ServiceIdConstants.RESOURCE, "region_hash");
    //oss文件的访问URL string结构
    public static final ZCache OSS_FILE_URL = new ZCache(ServiceIdConstants.RESOURCE, "oss_file_url");
    //数据字典 hash结构
    public static final ZCache DICT_DATA = new ZCache(ServiceIdConstants.RESOURCE, "dict_data");
    //OAuth客户端配置
    public static final ZCache OAUTH_CLIENT_DETAILS = new ZCache(ServiceIdConstants.AUTH, "oauth_client_details");
    //操作日志
    public static final ZCache SYS_OP_LOG = new ZCache(ServiceIdConstants.SYSTEM, "sys_op_log");


    private final String serviceId;

    private final String bizTag;

    public ZCache(String serviceId, String bizTag) {
        this.serviceId = serviceId;
        this.bizTag = bizTag;
    }

    public String key(Object key) {
        if (Objects.isNull(key) || StringUtils.isBlank(String.valueOf(key))) {
            throw new RuntimeException("key不能为空");
        }
        return MessageFormat.format("{0}:{1}:{2}:{3}", CommonConstants.ZEROSX, this.serviceId, this.bizTag, key);
    }

    public String key() {
        return MessageFormat.format("{0}:{1}:{2}", CommonConstants.ZEROSX, this.serviceId, this.bizTag);
    }

}
