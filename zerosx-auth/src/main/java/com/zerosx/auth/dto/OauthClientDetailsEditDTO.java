package com.zerosx.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName OauthClientDetailsEditDTO
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/27 12:58
 * @Version 1.0
 */
@Setter@Getter
public class OauthClientDetailsEditDTO {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Schema(description = "应用ID")
    private String clientId;

    @NotBlank
    @Schema(description = "应用名称")
    private String clientName;

    @NotBlank
    @Schema(description = "应用秘钥")
    private String clientSecret;

    @NotEmpty
    @Schema(description = "授权模式集合")
    private List<String> authorizedGrantTypes;

    @NotNull
    @Schema(description = "令牌时效(秒)")
    private Integer accessTokenValiditySeconds = 7200;

    @NotNull
    @Schema(description = "刷新令牌时效(秒)")
    private Integer refreshTokenValiditySeconds = 28800;

}
