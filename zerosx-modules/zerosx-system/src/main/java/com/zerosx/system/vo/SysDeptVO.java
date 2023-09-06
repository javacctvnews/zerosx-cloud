package com.zerosx.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-29 17:42:27
 */
@Getter
@Setter
@Schema(description = "部门表VO")
public class SysDeptVO implements Serializable {

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

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "角色ids")
    private List<Long> roleIds;

    public Long getParentId() {
        if (parentId == null || parentId == 0) {
            return null;
        }
        return parentId;
    }
}
