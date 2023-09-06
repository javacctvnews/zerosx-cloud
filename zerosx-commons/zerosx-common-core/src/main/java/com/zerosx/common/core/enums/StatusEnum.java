package com.zerosx.common.core.enums;

import com.zerosx.common.base.anno.AutoDictData;
import com.zerosx.common.base.enums.CodeEnum;
import lombok.Getter;

/**
 * StatusEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 13:11
 **/
@Getter
@AutoDictData(code = "StatusEnum", name = "状态")
public enum StatusEnum implements CodeEnum<String> {

    NORMAL("0", "正常"),

    ABNORMAL("1", "停用");

    private final String code;

    private final String message;

    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
