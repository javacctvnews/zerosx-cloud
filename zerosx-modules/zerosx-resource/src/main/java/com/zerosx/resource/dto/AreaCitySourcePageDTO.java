package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 行政区域数据源
 * @Description
 * @author javacctvnews
 * @date 2023-04-12 13:48:43
 */
@Getter
@Setter
@Schema(description = "行政区域数据源:分页查询DTO")
public class AreaCitySourcePageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "城市完整名称")
    private String areaName;

}
