package com.zerosx.common.core.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

@Getter
@AutoDictData(name = "行政区域层级")
public enum AreaDeepEnum implements BaseEnum<String> {

    PROVINCE("0", "省"),
    CITY("1", "市"),
    AREA("2", "区"),
    TOWN("3", "镇");

    private final String code;

    private final String message;

    AreaDeepEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
