package com.zerosx.common.log.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;

/**
 * 按钮-操作类型
 *
 * @author javacctvnews
 */
@AutoDictData(code = "OpTypeEnum", name = "操作类别")
public enum OpTypeEnum implements BaseEnum<String> {

    /**
     * 查询类型
     */
    QUERY("1", "查询"),

    /**
     * 新增类型
     */
    INSERT("2", "新增"),

    /**
     * 修改类型
     */
    UPDATE("3", "编辑"),

    /**
     * 删除类型
     */
    DELETE("4", "删除"),

    /**
     * 其它 类型
     */
    OTHER("5", "其他"),

    /**
     * 授权类型
     */
    GRANT("6", "授权"),

    /**
     * 导出类型
     */
    EXPORT("7", "导出"),

    /**
     * 导入类型
     */
    IMPORT("8", "导入"),

    /**
     * 强退
     */
    FORCE("9", "强退"),

    /**
     * 清空数据
     */
    CLEAN("10", "清空"),

    ;

    private final String code;

    private final String message;

    OpTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
