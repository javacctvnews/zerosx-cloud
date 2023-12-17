package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(name = "系统参数范围")
public enum ParamScopeEnum implements BaseEnum<String> {

    //全局范围
    GLOBAL("0", "全局"),
    //租户范围
    TENANT("1", "租户");

    private final String code;

    private final String message;

    ParamScopeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

}
