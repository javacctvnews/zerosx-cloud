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
 * 部门表
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 17:42:27
 */
@Getter
@Setter
@Schema(description = "部门表:分页结果对象")
public class SysDeptPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门id")
    @ExcelIgnore
    private Long id;

    @Schema(description = "部门名称")
    @ExcelProperty(value = {"部门名称"})
    private String deptName;

    @Schema(description = "部门编码")
    @ExcelProperty(value = {"部门编码"})
    private String deptCode;

    @Schema(description = "父部门id")
    @ExcelProperty(value = {"父部门id"})
    private Long parentId;

    @Schema(description = "祖级列表")
    @ExcelProperty(value = {"祖级列表"})
    private String ancestors;

    @Schema(description = "显示顺序")
    @ExcelProperty(value = {"显示顺序"})
    private Integer orderNum;

    @Schema(description = "负责人")
    @ExcelProperty(value = {"负责人"})
    private String leader;

    @Schema(description = "联系电话")
    @ExcelProperty(value = {"联系电话"})
    private String phone;

    @Schema(description = "邮箱")
    @ExcelProperty(value = {"邮箱"})
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    @ExcelProperty(value = {"部门状态（0正常 1停用）"})
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

    @Schema(description = "租户标识")
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
