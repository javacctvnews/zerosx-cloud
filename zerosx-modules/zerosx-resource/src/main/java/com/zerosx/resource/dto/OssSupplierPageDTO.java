package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * OSS配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-08 18:23:18
 */
@Getter
@Setter
@Schema(description = "OSS配置:分页查询DTO")
public class OssSupplierPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "oss服务商编码")
    private String supplierType;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "状态，0：正常；1：停用")
    private String status;

    @Schema(description = "租户标识")
    private String operatorId;

}
