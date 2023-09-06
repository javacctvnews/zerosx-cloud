package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.io.Serializable;

/**
 * 岗位管理
 * @Description
 * @author javacctvnews
 * @date 2023-07-28 15:40:01
 */
@Getter
@Setter
@Schema(description = "岗位管理:分页查询DTO")
public class SysPostPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operatorId;

    private String postName;

    private String status;

}
