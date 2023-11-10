package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 字典数据表
 * </p>
 */
@Data
public class SysDictDataDTO implements Serializable {

    @Schema(description = "字典数据排序")
    @NotNull(message = "字典数据排序为空")
    private Integer dictSort;

    @NotBlank(message = "字典数据标签为空")
    @Schema(description = "字典数据标签")
    private String dictLabel;

    @NotBlank(message = "字典数据键值为空")
    @Schema(description = "字典键值")
    private String dictValue;

    @NotBlank(message = "字典类型编码为空")
    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "状态（0正常 1停用）")
    @NotBlank(message = "字典数据状态为空")
    private String status;

    @Schema(description = "回显样式")
    private String listClass;

    @Schema(description = "样式属性")
    private String cssClass;

    @Schema(description = "备注")
    private String remarks;

}
