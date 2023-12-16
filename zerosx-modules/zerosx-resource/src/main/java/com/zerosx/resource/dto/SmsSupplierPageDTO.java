package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 短信配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-30 18:28:13
 */
@Getter
@Setter
@Schema(description = "短信配置:分页查询DTO")
public class SmsSupplierPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "状态，0：正常；1：停用")
    private String status;

    @Schema(description = "服务商名称")
    private String supplierName;

}
