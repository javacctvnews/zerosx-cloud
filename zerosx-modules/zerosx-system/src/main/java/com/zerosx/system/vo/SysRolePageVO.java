package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
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
@Schema(description = "角色信息表:分页结果对象")
public class SysRolePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户标识")
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;

    @Schema(description = "角色ID")
    @ExcelIgnore
    private Long id;

    @Schema(description = "角色名称")
    @ExcelProperty(value = {"角色名称"})
    private String roleName;

    @Schema(description = "角色权限字符串")
    @ExcelProperty(value = {"角色权限字符串"})
    private String roleKey;

    @Schema(description = "显示顺序")
    @ExcelProperty(value = {"显示顺序"})
    private Integer roleSort;

    @Schema(description = "菜单树选择项是否关联显示")
    @ExcelIgnore
    private Integer menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    @ExcelIgnore
    private Integer deptCheckStrictly;

    @Schema(description = "角色状态（0正常 1停用）")
    @ExcelProperty(value = {"角色状态"})
    private String status;

    @Schema(description = "创建者")
    @ExcelProperty(value = {"创建者"})
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "更新者")
    @ExcelProperty(value = {"更新者"})
    private String updateBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remark;

}
