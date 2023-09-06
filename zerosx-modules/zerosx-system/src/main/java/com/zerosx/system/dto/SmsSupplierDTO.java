package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 短信配置
 * @Description
 * @author javacctvnews
 * @date 2023-08-30 18:28:13
 */
@Getter
@Setter
@Schema(description = "SMS配置DTO")
public class SmsSupplierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="id")
    private Long id;

    @Schema(description ="服务商编码")
    private String supplierType;

    @Schema(description ="服务商名称")
    private String supplierName;

    @Schema(description ="状态，0：正常；1：停用")
    private String status;

    @Schema(description ="Access Key")
    private String accessKeyId;

    @Schema(description ="accessKeySecret")
    private String accessKeySecret;

    @Schema(description ="短信签名")
    private String signature;

    @Schema(description ="regionId")
    private String regionId;

    @Schema(description ="租户标识")
    @NotBlank(message = "租户公司不能为空")
    private String operatorId;

    @Schema(description ="sms请求地址")
    private String domainAddress;

    @Schema(description ="key值")
    private String keyValue;

    @Schema(description ="备注")
    private String remarks;

}
