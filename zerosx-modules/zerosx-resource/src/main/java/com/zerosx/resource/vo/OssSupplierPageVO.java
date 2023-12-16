package com.zerosx.resource.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.TranslConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * OSS配置
 * @Description
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "OSS配置:分页结果对象")
public class OssSupplierPageVO implements Serializable {

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
    @ExcelProperty(value = {"状态，0：正常；1：停用"})
    private String status;

    @Schema(description = "AccessKey")
    @ExcelProperty(value = {"AccessKey"})
    private String accessKeyId;

    /*@Schema(description = "AccessSecret")
    @ExcelIgnore
    @Sensitive(strategy = SensitiveStrategy.PASSWORD)
    private String accessKeySecret;*/

    @Schema(description = "存储桶名称")
    @ExcelProperty(value = {"存储桶名称"})
    private String bucketName;

    @Schema(description = "所属地域")
    @ExcelProperty(value = {"所属地域"})
    private String regionId;

    @Schema(description = "endpoint")
    @ExcelProperty(value = {"endpoint"})
    private String endpoint;

    @Schema(description = "域名")
    @ExcelProperty(value = {"域名"})
    private String domainAddress;

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
    private String remarks;

    @Schema(description = "租户标识")
    @Trans(type = TranslConstants.OPERATOR, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
