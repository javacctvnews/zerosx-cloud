package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 部门表
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 17:42:27
 */
@Getter
@Setter
@Schema(description = "部门表:分页查询DTO")
public class SysDeptPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operatorId;

    private String deptName;

    private String status;

}
