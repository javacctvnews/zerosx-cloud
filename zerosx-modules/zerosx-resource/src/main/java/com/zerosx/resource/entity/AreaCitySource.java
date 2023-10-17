package com.zerosx.resource.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 行政区域数据源
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-12 13:48:43
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_area_city_source")
public class AreaCitySource extends SuperEntity<AreaCitySource> {

    private static final long serialVersionUID = 1L;

    /**
     * 城市编号
     */
    private String areaCode;

    /**
     * 层级深度；0：省，1：市，2：区，3：镇
     */
    private Integer deep;

    /**
     * 上级编号
     */
    private String parentAreaCode;

    /**
     * 数据源原始的编号
     */
    private String extId;

    /**
     * 城市完整名称
     */
    private String areaName;

    @TableField(exist = false)
    private List<AreaCitySource> children;

}
