package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * EnableEnum
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-01 13:11
 **/
@Getter
@AutoDictData(code = "EnableEnum", name = "启用/停用")
public enum EnableEnum implements BaseEnum<Boolean> {

    TRUE(Boolean.TRUE, "启用", CssTypeEnum.PRIMARY.getCss()),

    FALSE(Boolean.FALSE, "关闭", CssTypeEnum.WARNING.getCss());

    private final Boolean code;

    private final String message;

    private final String css;

    EnableEnum(Boolean code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }

}
