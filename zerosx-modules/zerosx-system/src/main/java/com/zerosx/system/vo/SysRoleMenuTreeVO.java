package com.zerosx.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class SysRoleMenuTreeVO {

    @Schema(description="选择的ids")
    private List<Long> checkedKeys;

    @Schema(description="选择的id")
    private List<SysTreeSelectVO> menus;

}