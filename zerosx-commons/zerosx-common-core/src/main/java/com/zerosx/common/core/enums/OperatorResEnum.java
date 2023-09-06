package com.zerosx.common.core.enums;

import com.zerosx.common.base.anno.AutoDictData;
import com.zerosx.common.base.enums.CodeEnum;
import lombok.Getter;

/**
 * OperatorResEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 13:11
 **/
@Getter
@AutoDictData(code = "OperatorResEnum", name = "操作结果")
public enum OperatorResEnum implements CodeEnum<String> {

    NORMAL("0", "成功"),

    ABNORMAL("1", "失败");

    private final String code;

    private final String message;

    OperatorResEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
