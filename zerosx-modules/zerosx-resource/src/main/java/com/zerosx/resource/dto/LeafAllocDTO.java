package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * 美团分布式ID
 * @Description
 * @author javacctvnews
 * @date 2023-12-05 14:00:46
 */
@Getter
@Setter
@Schema(description = "美团分布式IDDTO")
public class LeafAllocDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="业务标识")
    private String bizTag;

    @Schema(description ="号段最大ID")
    private Long maxId;

    @Schema(description ="每次分配号段的步长")
    private Integer step;

    @Schema(description ="业务描述")
    private String description;

}
