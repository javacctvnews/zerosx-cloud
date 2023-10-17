package com.zerosx.common.core.enums.system;

import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
public enum MenuViewTypeEnum implements BaseEnum<String> {

    VIEW("view", "视图"),
    BUTTON("button", "按钮");

    private final String code;

    private final String message;

    MenuViewTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
