package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysMenuQueryDTO {

    @Schema(description = "系统用户id")
    private Long userId;

    @Schema(description = "目录名称")
    private String menuName;

    @Schema(description = "菜单状态：1-正常；0-停用")
    private Integer Status;

}
