package com.zerosx.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerosx.system.entity.SysDept;
import com.zerosx.system.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class SysTreeSelectVO {

    @Schema(description="节点id")
    private Long id;

    @Schema(description="节点名称")
    private String label;

    @Schema(description="子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysTreeSelectVO> children;

    public SysTreeSelectVO(SysMenu sysMenu) {
        this.id = sysMenu.getMenuId();
        this.label = sysMenu.getMenuName();
        if(CollectionUtils.isNotEmpty(sysMenu.getChildren())) {
            this.children = sysMenu.getChildren().stream().map(SysTreeSelectVO::new).collect(Collectors.toList());
        }
    }

    public SysTreeSelectVO(SysDept sysDept) {
        this.id = sysDept.getId();
        this.label = sysDept.getDeptName();
        if(CollectionUtils.isNotEmpty(sysDept.getChildren())) {
            this.children = sysDept.getChildren().stream().map(SysTreeSelectVO::new).collect(Collectors.toList());
        }
    }
}