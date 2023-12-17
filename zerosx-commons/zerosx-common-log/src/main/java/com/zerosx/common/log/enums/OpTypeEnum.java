package com.zerosx.common.log.enums;

import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import lombok.Getter;

/**
 * 按钮-操作类型
 *
 * @author javacctvnews
 */
@Getter
@AutoDictData(code = "OpTypeEnum", name = "操作类别")
public enum OpTypeEnum implements BaseEnum<String> {

    /**
     * 查询类型
     */
    QUERY("1", "查询", "primary"),

    /**
     * 新增类型
     */
    INSERT("2", "新增", "primary"),

    /**
     * 修改类型
     */
    UPDATE("3", "编辑", "success"),

    /**
     * 删除类型
     */
    DELETE("4", "删除", "danger"),

    /**
     * 其它 类型
     */
    OTHER("5", "其他", "primary"),

    /**
     * 授权类型
     */
    GRANT("6", "授权", "success"),

    /**
     * 导出类型
     */
    EXPORT("7", "导出", "warning"),

    /**
     * 导入类型
     */
    IMPORT("8", "导入", "primary"),

    /**
     * 强退
     */
    FORCE("9", "强退", "danger"),

    /**
     * 清空数据
     */
    CLEAN("10", "清空", "danger"),

    ;

    private final String code;

    private final String message;

    private final String css;

    OpTypeEnum(String code, String message, String css) {
        this.code = code;
        this.message = message;
        this.css = css;
    }

}
