package com.zerosx.common.core.enums.system;

import com.zerosx.common.core.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import com.zerosx.common.core.enums.CssTypeEnum;
import lombok.Getter;

@Getter
@AutoDictData(code = "MenuShowHideEnum", name = "菜单状态", desc = "控制左侧菜单的显示与隐藏")
public enum MenuShowHideEnum implements BaseEnum<String> {

    SHOW("0", "显示", CssTypeEnum.SUCCESS.getCss()),
    HIDDEN("1", "隐藏", CssTypeEnum.WARNING.getCss());

    private final String code;

    private final String message;

    private final String css;

    MenuShowHideEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }

}
