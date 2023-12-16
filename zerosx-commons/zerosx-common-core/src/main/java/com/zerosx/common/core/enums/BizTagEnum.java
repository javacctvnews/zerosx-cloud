package com.zerosx.common.core.enums;

import com.zerosx.common.core.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(name = "业务标识")
public enum BizTagEnum implements BaseEnum<String> {

    LEAF("leaf", "美团Leaf测试"),
    DEFAULT_BIZ("default_biz", "通用标识default"),
    USER_CODE("user_code", "系统管理用户编码ID"),
    OP_LOG("op_log", "操作日志"),
    ;

    private final String code;

    private final String message;

    BizTagEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCss() {
        return CssTypeEnum.DEFAULT.getCss();
    }

}
