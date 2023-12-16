package com.zerosx.common.core.enums;

import com.zerosx.common.core.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
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
public enum OperatorResEnum implements BaseEnum<String> {

    NORMAL("0", "成功", CssTypeEnum.PRIMARY.getCss()),

    ABNORMAL("1", "失败", CssTypeEnum.DANGER.getCss());

    private final String code;

    private final String message;

    private final String css;

    OperatorResEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }
}
