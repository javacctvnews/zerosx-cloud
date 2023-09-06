package com.zerosx.common.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ObjectNameQuery
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/22 15:23
 * @Version 1.0
 */
@Setter@Getter
public class ObjectNameQuery implements Serializable {

    private static final long serialVersionUID = 1279976767167185327L;

    //@Schema(description = "查询图片objectName的集合")
    private List<String> objectNames;

}
