package com.zerosx.api.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 前端select下拉框对象，支持i18n国际化
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description="select下拉框对象,支持i18n国际化")
public class I18nSelectOptionVO implements Serializable {

    @Schema(description = "键值")
    private Object value;

    @Schema(description = "标签")
    private String label;

    @Schema(description = "选中")
    private Boolean checked;

    @Schema(description = "子对象")
    private Object parentId;

    @Schema(description = "访问")
    private String isVirtual;

    @Schema(description = "国际化code")
    private String i18nCode;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "回显样式")
    private String listClass;

    @Schema(description = "样式属性")
    private String cssClass;

}
