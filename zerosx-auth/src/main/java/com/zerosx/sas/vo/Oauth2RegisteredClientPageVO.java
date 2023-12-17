package com.zerosx.sas.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-12 18:46:47
 */
@Getter
@Setter
@Schema(description = "客户端管理:分页结果对象")
public class Oauth2RegisteredClientPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @ExcelIgnore
    private String id;

    @Schema(description = "应用标识")
    @ExcelProperty(value = {"应用标识"})
    private String clientId;

    @Schema(description = "应用名称")
    @ExcelProperty(value = {"应用名称"})
    private String clientName;

    @Schema(description = "应用密钥(bcyt) 加密")
    @ExcelProperty(value = {"应用密钥(加密后)"})
    private String clientSecret;


    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date clientIdIssuedAt;


    @ExcelProperty(value = {"有效截止日期"})
    @Schema(description = "有效截止日期")
    private Date clientSecretExpiresAt;

    @Schema(description = "5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)")
    @ExcelProperty(value = {"授权方式"})
    private String authorizedGrantTypes;

    @Schema(description = "回调地址")
    @ExcelProperty(value = {"回调地址"})
    private String redirectUris;

    @Schema(description = "令牌有效期")
    @ExcelProperty(value = {"令牌有效期"})
    private Long accessTokenTimeToLive;

    @Schema(description = "刷新令牌有效期")
    @ExcelProperty(value = {"刷新令牌有效期"})
    private Long refreshTokenTimeToLive;

    @Schema(description = "授权码令牌有效期")
    @ExcelProperty(value = {"授权码令牌有效期"})
    private Long authorizationCodeTimeToLive;

    @Schema(description = "设备码令牌有效期")
    @ExcelProperty(value = {"设备码令牌有效期"})
    private Long deviceCodeTimeToLive;

    @Schema(description = "是否启用刷新令牌")
    @ExcelProperty(value = {"是否启用刷新令牌"})
    private Boolean reuseRefreshTokens;

    @Schema(description = "IdToken加密算法")
    @ExcelProperty(value = {"IdToken加密算法"})
    private String idTokenSignatureAlgorithm;

}
