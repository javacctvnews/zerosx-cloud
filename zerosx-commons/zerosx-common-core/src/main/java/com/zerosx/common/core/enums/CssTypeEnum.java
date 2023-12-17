package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(code = "CssTypeEnum", name = "CSS样式")
public enum CssTypeEnum implements BaseEnum<String> {

    DEFAULT("default", "默认", "default"),
    PRIMARY("primary", "主要", "primary"),
    SUCCESS("success", "成功", "success"),
    INFO("info", "信息", "info"),
    WARNING("warning", "警告", "warning"),
    DANGER("danger", "危险", "danger");

    private final String code;

    private final String message;

    private final String css;

    CssTypeEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }

}
