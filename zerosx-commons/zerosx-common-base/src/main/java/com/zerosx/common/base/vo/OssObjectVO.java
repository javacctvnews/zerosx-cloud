package com.zerosx.common.base.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * oss统一返回的对象
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OssObjectVO {

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 对象查看路径
     */
    private String objectPath;

    /**
     * 文件查看URL
     */
    private String objectViewUrl;

}
