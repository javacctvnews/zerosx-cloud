package com.zerosx.api.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -1451702073679771565L;
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号码")
    private String mobilePhone;

}
