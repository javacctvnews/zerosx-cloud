package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 行政区域数据源
 * @Description
 * @author javacctvnews
 * @date 2023-04-12 13:48:43
 */
@Getter
@Setter
@Schema(description = "行政区域数据源DTO")
public class AreaCitySourceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "城市编号")
    private Long id;

    @Schema(description = "城市编号")
    @NotBlank(message = "城市编号不能为空")
    private String areaCode;

    @Schema(description = "层级深度；0：省，1：市，2：区，3：镇")
    private Integer deep;

    @Schema(description = "上级编号")
    private String parentAreaCode;

    @Schema(description = "数据源原始的编号")
    private String extId;

    @Schema(description = "城市完整名称")
    private String areaName;

}
