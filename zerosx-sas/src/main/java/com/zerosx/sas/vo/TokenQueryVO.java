package com.zerosx.sas.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data@Schema(description = "token查询条件")
public class TokenQueryVO implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "客户端ID不能为空")
    @Schema(description = "客户端ID")
    private String clientId;

    private List<String> tokens;

    @Schema(description = "清空类型，0：所有；1：过期token(默认)")
    private String opType = "1";

}
