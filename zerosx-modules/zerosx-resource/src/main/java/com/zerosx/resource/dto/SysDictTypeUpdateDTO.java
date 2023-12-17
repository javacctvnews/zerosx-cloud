package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "字典类型表")
public class SysDictTypeUpdateDTO implements Serializable {

    @Schema(description = "字典类型ID")
    @NotNull
    private Long id;

    @Schema(description = "字典名称")
    @NotBlank
    private String dictName;

    @Schema(description = "状态（0正常 1停用）")
    @NotNull
    private String dictStatus;

    @Schema(description = "备注")
    private String remarks;

}
