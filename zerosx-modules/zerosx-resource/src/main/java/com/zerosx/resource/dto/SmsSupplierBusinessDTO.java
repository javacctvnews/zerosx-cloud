package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
@Getter
@Setter
@Schema(description = "短信业务模板DTO")
public class SmsSupplierBusinessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="记录ID")
    private Long id;

    @Schema(description ="服务商ID")
    @NotNull(message = "服务商ID不能为空")
    private Long smsSupplierId;

    @Schema(description ="短信业务编码")
    @NotBlank(message = "短信业务编码不能为空")
    private String businessCode;

    @Schema(description ="短信模板编号")
    private String templateCode;

    @Schema(description ="模板内容")
    private String templateContent;

    @Schema(description ="模板签名")
    private String signature;

    @Schema(description ="状态，0：正常；1：停用")
    private String status;

    @Schema(description ="备注")
    private String remarks;

    @Schema(description ="租户标识")
    private String operatorId;

}
