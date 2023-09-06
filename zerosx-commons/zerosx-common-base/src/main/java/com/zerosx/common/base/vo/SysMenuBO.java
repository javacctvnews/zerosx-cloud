package com.zerosx.common.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName SysMenuBO
 * @Description 系统菜单按钮
 * @Author javacctvnews
 * @Date 2023/3/24 21:13
 * @Version 1.0
 */
@Getter
@Setter
public class SysMenuBO implements Serializable {

    private static final long serialVersionUID = -8315881755929451808L;
    /**
     * 访问地址
     */
    private String requestUrl;

    /**
     * 请求的类型
     */
    private String requestMethod;

}
