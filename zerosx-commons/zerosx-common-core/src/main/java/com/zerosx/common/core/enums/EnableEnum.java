package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * EnableEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 13:11
 **/
@Getter
@AutoDictData(code = "EnableEnum", name = "启用/停用")
public enum EnableEnum implements BaseEnum<Boolean> {

    TRUE(Boolean.TRUE, "启用"),

    FALSE(Boolean.FALSE, "关闭");

    private final Boolean code;

    private final String message;

    EnableEnum(Boolean code, String message) {
        this.code = code;
        this.message = message;
    }

}
