package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 菜单权限表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 14:49:30
 */
@Getter
@Setter
@Schema(description = "菜单权限表:分页查询DTO")
public class SysMenuPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
}
