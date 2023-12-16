package com.zerosx.common.core.enums;

import com.zerosx.common.core.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
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
public enum StatusEnum implements BaseEnum<String> {

    NORMAL("0", "正常", CssTypeEnum.PRIMARY.getCss()),

    ABNORMAL("1", "停用", CssTypeEnum.WARNING.getCss());

    private final String code;

    private final String message;

    private final String css;

    StatusEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }
}
