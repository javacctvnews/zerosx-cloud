package com.zerosx.system.dto;

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
@Schema(description = "OSS配置DTO")
public class OssSupplierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="id")
    private Long id;

    @Schema(description ="服务商编码")
    private String supplierType;

    @Schema(description ="服务商名称")
    private String supplierName;

    @Schema(description ="状态，0：正常；1：停用")
    private String status;

    @Schema(description ="AccessKey")
    private String accessKeyId;

    @Schema(description ="AccessSecret")
    private String accessKeySecret;

    @Schema(description ="存储桶名称")
    private String bucketName;

    @Schema(description ="所属地域")
    private String regionId;

    @Schema(description ="endpoint")
    private String endpoint;

    @Schema(description ="域名")
    private String domainAddress;

    @Schema(description ="备注")
    private String remarks;

    @Schema(description ="租户标识")
    private String operatorId;

}
