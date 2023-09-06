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
 * 岗位管理
 * @Description
 * @author javacctvnews
 * @date 2023-07-28 15:40:01
 */
@Getter
@Setter
@Schema(description = "岗位管理:分页结果对象")
public class SysPostPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "岗位ID")
    @ExcelIgnore
    private Long id;

    @Schema(description = "租户标识")
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;

    @Schema(description = "岗位编码")
    @ExcelProperty(value = {"岗位编码"})
    private String postCode;

    @Schema(description = "岗位名称")
    @ExcelProperty(value = {"岗位名称"})
    private String postName;

    @Schema(description = "显示顺序")
    @ExcelProperty(value = {"显示顺序"})
    private Integer postSort;

    @Schema(description = "状态（0正常 1停用）")
    @ExcelProperty(value = {"状态（0正常 1停用）"})
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
