package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门表
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 17:42:27
 */
@Getter
@Setter
@Schema(description = "部门表DTO")
public class SysDeptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门id")
    private Long id;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门编码")
    private String deptCode;

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    private String status;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "角色ids")
    private List<Long> roleIds;
}
