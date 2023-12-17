package com.zerosx.sas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-09 15:33:32
 */
@Getter
@Setter
@Schema(description = "登录日志:分页查询DTO")
public class OauthTokenRecordPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "登录地址关键字")
    private String sourceLocation;
    @Schema(description = "用户名关键字")
    private String username;
    @Schema(description = "登录状态")
    private Integer status;
    @Schema(description = "登录开始时间")
    private String beginOauthApplyTime;
    @Schema(description = "登录结束时间")
    private String endOauthApplyTime;

}
