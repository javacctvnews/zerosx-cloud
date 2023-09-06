package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysRoleMenuQueryDTO {

    @Schema(description = "系统用户id")
    private Long userId;

    @Schema(description = "角色id")
    private Long roleId;

}
