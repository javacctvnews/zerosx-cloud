package com.zerosx.common.core.enums.system;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import com.zerosx.common.core.enums.CssTypeEnum;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AutoDictData(name = "用户类型")
public enum UserTypeEnum implements BaseEnum<String> {

    /**
     * 平台超级管理员，拥有全部的功能权限，数据权限按配置
     */
    SUPER_ADMIN("super_admin", "超级管理员"),

    /**
     * SaaS操作员，功能权限、数据权限按配置
     */
    SAAS_OPERATOR("saas_operator", "SaaS管理员"),

    /**
     * 租户操作员，功能权限、数据权限按配置
     */
    TENANT_OPERATOR("tenant_operator", "租户操作员"),

    ;


    private final String code;

    private final String message;

    UserTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

    public static String getKeyByName(String obj) {
        UserTypeEnum[] values = UserTypeEnum.values();
        for (UserTypeEnum value : values) {
            if (value.getCode().equals(obj)) {
                return value.getMessage();
            }
        }
        return "";
    }

}
