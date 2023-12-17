package com.zerosx.sas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Oauth2RegisteredClientDTO
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-11-08 11:12
 **/
@Getter
@Setter
@Schema(description = "客户端管理DTO")
public class Oauth2RegisteredClientDTO implements Serializable {

    private static final long serialVersionUID = -2800372077520233322L;

    private String id;
    private String clientId;
    private String clientName;
    private String clientSecret;

    @Schema(description = "有效截止日期")
    private Date clientSecretExpiresAt;

    @Schema(description = "状态，0：正常，1：停用")
    private String status;

    @Schema(description = "资源权限集合")
    private List<String> resourceIdList;

    @Schema(description = "授权模式集合")
    private Set<String> authorizationGrantTypes;

    @Schema(description = "回调地址")
    private String redirectUris;

    // clientSettings
    private String requireProofKey;

    // tokenSettings
    @Schema(description = "accessToken有效期")
    private Integer accessTokenTimeToLive;
    @Schema(description = "refreshToken有效期")
    private Integer refreshTokenTimeToLive;
    @Schema(description = "授权码令牌有效期")
    private Integer authorizationCodeTimeToLive;
    @Schema(description = "设备码令牌有效期")
    private Integer deviceCodeTimeToLive;
    @Schema(description = "是否启用刷新令牌")
    private String reuseRefreshTokens;
    @Schema(description = "IdToken加密算法")
    private String idTokenSignatureAlgorithm;

}
