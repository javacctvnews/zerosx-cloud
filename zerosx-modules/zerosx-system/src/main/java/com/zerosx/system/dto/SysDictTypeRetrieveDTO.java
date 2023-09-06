package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
public class SysDictTypeRetrieveDTO implements Serializable {

    private static final long serialVersionUID = 269828248514856113L;

    @Schema(description = "字典类型名称")
    private String dictName;

    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "状态（0正常 1停用）")
    private String dictStatus;

}
