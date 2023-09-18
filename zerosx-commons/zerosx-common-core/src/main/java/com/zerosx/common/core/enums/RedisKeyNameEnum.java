package com.zerosx.common.core.enums;

import com.zerosx.common.base.constants.ServiceIdConstants;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * redis的key定义
 * <p>
 * 采用枚举是为了更加命名规范
 */
@Getter
public enum RedisKeyNameEnum {

    /**
     * 用户名密码+验证码登录授权 存储验证码 60秒有效
     * string结构
     */
    IMAGE_CODE(ServiceIdConstants.AUTH, "image_code"),
    /**
     * 短信验证码
     */
    SMS_CODE(ServiceIdConstants.AUTH, "sms_code"),

    /**
     * 当前登录用户基础信息（角色集合和租户信息）
     */
    CURRENT_USER(ServiceIdConstants.SYSTEM, "current_user_data"),
    /**
     * 角色对应的权限集合
     */
    ROLE_PERMISSIONS(ServiceIdConstants.SYSTEM, "role_permissions"),

    /**
     * 省市区 string结构
     */
    REGION(ServiceIdConstants.SYSTEM, "region"),
    /**
     * 省市区 hash结构
     * hashkey为code
     */
    REGION_HASH(ServiceIdConstants.SYSTEM, "region_hash"),

    /**
     * oss文件的访问URL string结构
     */
    OSS_FILE_URL(ServiceIdConstants.SYSTEM, "oss_file_url"),

    /**
     * 数据字典 hash结构
     */
    DICT_DATA(ServiceIdConstants.SYSTEM, "dict_data"),

    /**
     * 分布式锁前缀
     */
    LOCK("RedissonDistributedLock", "lock_key", ""),
    /**
     * 客户端配置
     */
    OAUTH_CLIENT_DETAILS(ServiceIdConstants.AUTH, "oauth_client_details"),
    /**
     * 运营商缓存
     */
    OPERATOR(ServiceIdConstants.SYSTEM, "operator", ""),
    ;

    /**
     * 服务名称
     * ServiceNameConstants
     */
    private final String serviceId;
    /**
     * 业务标识符
     */
    private final String bzId;

    /**
     * hashKey
     */
    private String field = "";

    RedisKeyNameEnum(String serviceId, String bzId, String field) {
        this.serviceId = serviceId;
        this.bzId = bzId;
        this.field = field;
    }

    RedisKeyNameEnum(String serviceId, String bzId) {
        this.serviceId = serviceId;
        this.bzId = bzId;
    }

    public static String key(RedisKeyNameEnum keyEnum, Object keyCode) {
        if (keyCode != null && StringUtils.isNotBlank(keyCode.toString())) {
            return keyEnum.getServiceId() + ":" + keyEnum.bzId + ":" + keyCode;
        }
        return keyEnum.getServiceId() + ":" + keyEnum.bzId;
    }

    public static String key(RedisKeyNameEnum keyEnum) {
        return key(keyEnum, null);
    }

    /**
     * hash存储时的hash key 命名规则
     *
     * @param keyEnum
     * @return
     */
    public static String keyHash(RedisKeyNameEnum keyEnum) {
        return keyEnum.getServiceId() + ":" + keyEnum.getBzId();
    }

}
