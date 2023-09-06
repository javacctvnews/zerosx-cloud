package com.zerosx.system.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName AreaCitySourceTreeVO
 * @Description 行政区域源数据树形结构
 * @Author javacctvnews
 * @Date 2023/4/14 11:30
 * @Version 1.0
 */
@Setter
@Getter
public class AreaCitySourceTreeVO {

    private String id;

    private String key;

    private String parentId;

    private String parentTitle;

    private String title;

    private String value;
    //是否有子节点
    private boolean hasChildren;
    //层级
    private Integer deep;

}
