package com.zerosx.resource.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 行政区域数据源
 * @Description
 * @author javacctvnews
 * @date 2023-04-12 13:48:43
 */
@Getter
@Setter
@Schema(description = "行政区域数据源:分页结果对象")
public class AreaCitySourcePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "城市编号")
    @ExcelIgnore
    private Long id;

    @Schema(description = "城市编号")
    @ExcelProperty(value = {"城市编号"})
    private String areaCode;

    @Schema(description = "层级深度；0：省，1：市，2：区，3：镇")
    @ExcelProperty(value = {"层级深度；0：省，1：市，2：区，3：镇"})
    private Integer deep;

    @Schema(description = "上级编号")
    @ExcelProperty(value = {"上级编号"})
    private String parentAreaCode;

    @Schema(description = "数据源原始的编号")
    @ExcelProperty(value = {"数据源原始的编号"})
    private String extId;

    @Schema(description = "城市完整名称")
    @ExcelProperty(value = {"城市完整名称"})
    private String areaName;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "最新更新时间")
    @ExcelProperty(value = {"最新更新时间"})
    private Date updateTime;

}
