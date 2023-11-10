package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Schema(description = "字典类型表")
public class SysDictTypeDTO implements Serializable {

    private static final long serialVersionUID = 3469914432781029103L;

    @Schema(description = "字典类型名称")
    @NotBlank
    private String dictName;

    @NotBlank
    @Schema(description = "字典类型编码")
    private String dictType;

    @NotBlank
    @Schema(description = "状态（0正常 1停用）")
    private String dictStatus;

    @NotBlank
    @Schema(description = "备注")
    private String remarks;

}
