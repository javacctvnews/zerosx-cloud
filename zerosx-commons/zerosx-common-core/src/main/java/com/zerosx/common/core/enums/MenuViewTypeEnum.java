package com.zerosx.common.core.enums;

import com.zerosx.common.base.enums.CodeEnum;
import lombok.Getter;

@Getter
public enum MenuViewTypeEnum implements CodeEnum<String> {

    VIEW("view", "视图"),
    BUTTON("button", "按钮");

    private final String code;

    private final String message;

    MenuViewTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
