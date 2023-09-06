package com.zerosx.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息表
 * @Description
 * @author javacctvnews
 * @date 2023-07-27 17:53:15
 */
@Getter
@Setter
@Schema(description = "角色信息表VO")
public class SysRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限字符串")
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "角色权限")
    private SysRoleMenuTreeVO sysRoleMenuTreeVO;
}
