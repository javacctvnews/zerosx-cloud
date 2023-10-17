package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * @ClassName AuthTypeEnum
 * @Description 认证授权用户类型
 * 场景：针对不同的用户授权时查询的数据库不一样，故扩展授权增加账户类型。多个系统共用zero-auth情况下，此模式也适用，将授权账户类型做区分即可
 * @Author javacctvnews
 * @Date 2023/3/25 22:44
 * @Version 1.0
 */
@Getter
@AutoDictData(name = "用户授权类型")
public enum AuthTypeEnum implements BaseEnum<String> {

    /**
     * SaaS平台登录用户
     */
    SYS_USER("SysUser", "系统用户"),

    ;


    private final String code;

    private final String message;

    AuthTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String obj) {
        AuthTypeEnum[] values = AuthTypeEnum.values();
        for (AuthTypeEnum value : values) {
            if (value.getCode().equals(obj)) {
                return value.getMessage();
            }
        }
        return "";
    }

}
