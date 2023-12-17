package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(name = "行政区域层级")
public enum AreaDeepEnum implements BaseEnum<String> {

    PROVINCE("0", "省", CssTypeEnum.PRIMARY.getCss()),
    CITY("1", "市", CssTypeEnum.PRIMARY.getCss()),
    AREA("2", "区", CssTypeEnum.PRIMARY.getCss()),
    TOWN("3", "镇", CssTypeEnum.PRIMARY.getCss());

    private final String code;

    private final String message;

    private final String css;

    AreaDeepEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }

}
