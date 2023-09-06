package com.zerosx.common.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName Region
 * @Description 中国行政区域代码表
 * @Author javacctvnews
 * @Date 2023/3/15 10:56
 * @Version 1.0
 */
@Setter@Getter
public class RegionSelectVO implements Serializable {

    private static final long serialVersionUID = 5260002209562792523L;
    //区域编码
    private String areaCode;
    //区域编码名称
    private String areaName;
    //区域的子区域
    private List<RegionSelectVO> children;

}
