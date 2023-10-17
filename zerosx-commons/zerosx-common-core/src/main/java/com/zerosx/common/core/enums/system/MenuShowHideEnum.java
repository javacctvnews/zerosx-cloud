package com.zerosx.common.core.enums.system;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(code = "MenuShowHideEnum", name = "菜单状态")
public enum MenuShowHideEnum implements BaseEnum<String> {

    SHOW("0", "显示"),
    HIDDEN("1", "隐藏");

    private final String code;

    private final String message;

    MenuShowHideEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
