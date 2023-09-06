package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色信息表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-27 17:53:15
 */
@Getter
@Setter
@Schema(description = "角色信息表:分页查询DTO")
public class SysRolePageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operatorId;

    private String roleKeyword;

    private String status;

}
