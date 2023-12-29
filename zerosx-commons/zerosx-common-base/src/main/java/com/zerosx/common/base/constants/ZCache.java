package com.zerosx.common.base.constants;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 2320358632082520428L;

    //图形验证码默认过期时间，单位：秒
    public static final Long DEFAULT_EXPIRE = 300L;

    //运营商
    public static final ZCache OPERATOR = new ZCache(ZCacheKey.OPERATOR);

    //图形验证码
    public static final ZCache CAPTCHA = new ZCache(ZCacheKey.CAPTCHA);
    //防重令牌
    public static final ZCache IDEMPOTENT_TOKEN = new ZCache(ZCacheKey.IDEMPOTENT_TOKEN);

    //部门
    public static final ZCache DEPT = new ZCache(ZCacheKey.DEPT);
    //短信验证码
    public static final ZCache SMS_CODE = new ZCache(ZCacheKey.SMS_CODE);
    //当前登录用户基础信息（角色集合和租户信息）
    public static final ZCache CURRENT_USER = new ZCache(ZCacheKey.CURRENT_USER);
    //角色对应的权限集合
    public static final ZCache ROLE_PERMISSIONS = new ZCache(ZCacheKey.ROLE_PERMISSIONS);
    //省市区 json字符串结构
    public static final ZCache REGION = new ZCache(ZCacheKey.REGION);
    //省市区 hash结构
    public static final ZCache REGION_HASH = new ZCache(ZCacheKey.REGION_HASH);
    //oss文件的访问URL string结构
    public static final ZCache OSS_FILE_URL = new ZCache(ZCacheKey.OSS_FILE_URL);
    //数据字典 hash结构
    public static final ZCache DICT_DATA = new ZCache(ZCacheKey.DICT_DATA);
    //OAuth客户端配置
    public static final ZCache OAUTH_CLIENT_DETAILS = new ZCache(ZCacheKey.OAUTH_CLIENT_DETAILS);
    //操作日志
    public static final ZCache SYS_OP_LOG = new ZCache(ZCacheKey.SYS_OP_LOG);

    /* sas认证 start */
    //access_token
    public static final ZCache SAS_ACCESS_TOKEN = new ZCache(ZCacheKey.SAS_ACCESS_TOKEN);
    //refresh_token
    public static final ZCache SAS_REFRESH_TOKEN = new ZCache(ZCacheKey.SAS_REFRESH_TOKEN);
    //token分页
    public static final ZCache SAS_TOKEN_PAGE = new ZCache(ZCacheKey.SAS_TOKEN_PAGE);
    /* sas认证 end */

    private final String cacheKey;

    public ZCache(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String key(Object key) {
        if (Objects.isNull(key) || StringUtils.isBlank(String.valueOf(key))) {
            throw new RuntimeException("key不能为空");
        }
        return MessageFormat.format("{0}:{1}", this.cacheKey, key);
    }

    public String key() {
        return this.cacheKey;
    }

}
