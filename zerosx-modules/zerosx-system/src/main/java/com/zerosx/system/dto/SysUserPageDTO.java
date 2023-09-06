package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.io.Serializable;

/**
 * 系统用户
 * @Description
 * @author javacctvnews
 * @date 2023-07-20 13:48:04
 */
@Getter
@Setter
@Schema(description = "系统用户:分页查询DTO")
public class SysUserPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operatorId;

    private String userKeyword;

    private String status;

    private String phoneNumber;
}
