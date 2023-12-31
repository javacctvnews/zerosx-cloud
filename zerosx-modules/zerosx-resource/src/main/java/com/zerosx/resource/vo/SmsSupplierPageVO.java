package com.zerosx.resource.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.core.anno.Sensitive;
import com.zerosx.common.core.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信配置
 * @Description
 * @author javacctvnews
 * @date 2023-08-30 18:28:13
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "短信配置:分页结果对象")
public class SmsSupplierPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "服务商编码")
    @ExcelProperty(value = {"服务商编码"})
    private String supplierType;

    @Schema(description = "服务商名称")
    @ExcelProperty(value = {"服务商名称"})
    private String supplierName;

    @Schema(description = "状态，0：正常；1：停用")
    @ExcelProperty(value = {"状态"})
    private String status;

    @Schema(description = "Access Key")
    @ExcelProperty(value = {"Access Key"})
    private String accessKeyId;

    /*@Schema(description = "accessKeySecret")
    @ExcelIgnore
    @Sensitive(strategy = SensitiveStrategy.PASSWORD)
    private String accessKeySecret;*/

    @Schema(description = "短信签名")
    @ExcelProperty(value = {"短信签名"})
    private String signature;

    @Schema(description = "regionId")
    @ExcelProperty(value = {"regionId"})
    private String regionId;

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
    @Trans(type = TranslConstants.OPERATOR, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;

    @Schema(description ="sms请求地址")
    @ExcelProperty(value = {"sms请求地址"})
    private String domainAddress;

    @Schema(description ="key值")
    @ExcelProperty(value = {"key值"})
    @Sensitive(strategy = SensitiveStrategy.PASSWORD)
    private String keyValue;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remarks;


}
