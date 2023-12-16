package com.zerosx.common.core.enums;

import com.zerosx.common.core.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(code = "SexEnum", name = "性别")
public enum SexEnum implements BaseEnum<String> {

    MALE("0", "男"),
    FEMALE("1", "女"),
    UNKNOWN("2", "未知");

    private final String code;

    private final String message;

    SexEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

}
