package com.zerosx.sas.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 客户端管理
 * @Description
 * @author javacctvnews
 * @date 2023-08-12 18:46:47
 */
@Getter
@Setter
@Schema(description = "客户端管理VO")
public class Oauth2RegisteredClientVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Long accessTokenTimeToLive;
    @Schema(description = "refreshToken有效期")
    private Long refreshTokenTimeToLive;
    @Schema(description = "授权码令牌有效期")
    private Long authorizationCodeTimeToLive;
    @Schema(description = "设备码令牌有效期")
    private Long deviceCodeTimeToLive;
    @Schema(description = "是否启用刷新令牌")
    private String reuseRefreshTokens;
    @Schema(description = "IdToken加密算法")
    private String idTokenSignatureAlgorithm;

}
