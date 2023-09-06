package com.zerosx.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 客户端管理
 * @Description
 * @author javacctvnews
 * @date 2023-08-12 18:46:47
 */
@Getter
@Setter
@Schema(description = "客户端管理:分页查询DTO")
public class OauthClientDetailsPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="应用标识")
    private String clientId;

    @Schema(description ="应用名称")
    private String clientName;

    @Schema(description ="范围")
    private String scope;

    @Schema(description ="5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)")
    private String authorizedGrantTypes;

    @Schema(description ="状态，0：正常，1：停用")
    private String status;
}
