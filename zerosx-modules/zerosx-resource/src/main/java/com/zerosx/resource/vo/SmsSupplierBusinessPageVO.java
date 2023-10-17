package com.zerosx.resource.vo;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "短信业务模板:分页结果对象")
public class SmsSupplierBusinessPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "服务商ID")
    @ExcelProperty(value = {"服务商ID"})
    private Long smsSupplierId;

    @Schema(description = "短信业务编码")
    @ExcelProperty(value = {"短信业务编码"})
    private String businessCode;

    @Schema(description = "短信模板编号")
    @ExcelProperty(value = {"短信模板编号"})
    private String templateCode;

    @Schema(description = "模板内容")
    @ExcelProperty(value = {"模板内容"})
    private String templateContent;

    @Schema(description = "模板签名")
    @ExcelProperty(value = {"模板签名"})
    private String signature;

    @Schema(description = "状态，0：正常；1：停用")
    @ExcelProperty(value = {"状态，0：正常；1：停用"})
    private String status;

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

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remarks;

    @Schema(description = "租户标识")
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
