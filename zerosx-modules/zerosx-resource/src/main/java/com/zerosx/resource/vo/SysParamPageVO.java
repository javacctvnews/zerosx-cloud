package com.zerosx.resource.vo;

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
 * 系统参数
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 01:02:29
 */
@Getter
@Setter
@Schema(description = "系统参数:分页结果对象")
public class SysParamPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    @ExcelIgnore
    private Long id;

    @Schema(description = "参数名")
    @ExcelProperty(value = {"参数名"})
    private String paramName;

    @Schema(description = "参数编码")
    @ExcelProperty(value = {"参数编码"})
    private String paramKey;

    @Schema(description = "参数值")
    @ExcelProperty(value = {"参数值"})
    private String paramValue;

    @Schema(description = "参数范围，0：全局，1：租户")
    @ExcelProperty(value = {"参数范围，0：全局，1：租户"})
    private String paramScope;

    @Schema(description = "状态，0：正常，1：停用")
    @ExcelProperty(value = {"状态，0：正常，1：停用"})
    private String status;

    @Schema(description = "备注说明")
    @ExcelProperty(value = {"备注说明"})
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "创建人")
    @ExcelProperty(value = {"创建人"})
    private String createBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "更新人")
    @ExcelProperty(value = {"更新人"})
    private String updateBy;

    @Schema(description = "租户标识")
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
