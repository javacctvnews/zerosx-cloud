package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author junmy
 * @since 2020-11-18
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
