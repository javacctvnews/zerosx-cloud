package com.zerosx.common.core.enums;


import com.zerosx.common.base.anno.AutoDictData;
import com.zerosx.common.base.enums.CodeEnum;
import lombok.Getter;


@Getter
@AutoDictData(name = "菜单类型")
public enum MenuTypeEnum implements CodeEnum<String> {

    TYPE_M("M", "目录"),
    TYPE_C("C", "菜单"),
    TYPE_F("F", "按钮");

    private final String code;

    private final String message;

    MenuTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getKeyByName(String obj) {
        MenuTypeEnum[] values = MenuTypeEnum.values();
        for (MenuTypeEnum value : values) {
            if (value.code.equals(obj)) {
                return value.message;
            }
        }
        return "";
    }

}
