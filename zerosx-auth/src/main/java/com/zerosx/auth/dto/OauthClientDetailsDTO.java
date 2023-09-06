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
@Schema(description = "客户端管理DTO")
public class OauthClientDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="主键")
    private Long id;

    @Schema(description ="应用标识")
    private String clientId;

    @Schema(description ="应用名称")
    private String clientName;

    @Schema(description ="资源限定串(逗号分割)")
    private String resourceIds;

    @Schema(description ="应用密钥(bcyt) 加密")
    private String clientSecret;

    @Schema(description ="应用密钥(明文)")
    private String clientSecretStr;

    @Schema(description ="范围")
    private String scope;

    @Schema(description ="5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)")
    private String authorizedGrantTypes;

    @Schema(description ="回调地址 ")
    private String webServerRedirectUri;

    @Schema(description ="权限")
    private String authorities;

    @Schema(description ="access_token有效期")
    private Integer accessTokenValidity;

    @Schema(description ="refresh_token有效期")
    private Integer refreshTokenValidity;

    @Schema(description ="附加信息")
    private String additionalInformation;

    @Schema(description ="是否自动授权 是-true")
    private String autoapprove;

    @Schema(description ="状态，0：正常，1：停用")
    private String status;

}
