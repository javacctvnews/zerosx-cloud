package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 字典数据表
 */
@Setter
@Getter
@Schema(description = "SysDictDataUpdateDTO对象")
public class SysDictDataUpdateDTO implements Serializable {

    @Schema(description = "字典数据ID")
    @NotNull(message = "id为空")
    private Long id;

    @Schema(description = "字典排序")
    @NotNull(message = "字典排序为空")
    private Integer dictSort;

    @NotBlank(message = "字典数据键值为空")
    private String dictValue;

    @Schema(description = "字典标签")
    @NotBlank(message = "字典数据标签为空")
    private String dictLabel;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "字典数据状态状态（0正常 1停用）")
    @NotBlank(message = "字典数据状态为空")
    private String status;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "回显样式")
    private String listClass;

    @Schema(description = "样式属性")
    private String cssClass;

}
