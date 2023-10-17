package com.zerosx.api.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDictDataPageDTO implements Serializable {

    @Schema(description = "字典类型编码")
    private String dictType;

    @Schema(description = "字典数据标签")
    private String dictLabel;

    @Schema(description = "状态（0正常 1停用）")
    private String status;
}
