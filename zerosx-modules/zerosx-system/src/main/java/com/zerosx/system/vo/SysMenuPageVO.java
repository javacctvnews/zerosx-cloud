package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限表
 * @Description
 * @author javacctvnews
 * @date 2023-07-20 14:49:30
 */
@Getter
@Setter
@Schema(description = "菜单权限表:分页结果对象")
public class SysMenuPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @ExcelIgnore
    private Long menuId;

    @Schema(description = "菜单名称")
    @ExcelProperty(value = {"菜单名称"})
    private String menuName;

    @Schema(description = "父菜单ID")
    @ExcelProperty(value = {"父菜单ID"})
    private Long parentId;

    @Schema(description = "显示顺序")
    @ExcelProperty(value = {"显示顺序"})
    private Integer orderNum;

    @Schema(description = "路由地址")
    @ExcelProperty(value = {"路由地址"})
    private String path;

    @Schema(description = "组件路径")
    @ExcelProperty(value = {"组件路径"})
    private String component;

    @Schema(description = "路由参数")
    @ExcelProperty(value = {"路由参数"})
    private String queryParam;

    @Schema(description = "是否为外链（0是 1否）")
    @ExcelProperty(value = {"是否为外链（0是 1否）"})
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    @ExcelProperty(value = {"是否缓存（0缓存 1不缓存）"})
    private Integer isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    @ExcelProperty(value = {"菜单类型（M目录 C菜单 F按钮）"})
    private String menuType;

    @Schema(description = "显示状态（0显示 1隐藏）")
    @ExcelProperty(value = {"显示状态（0显示 1隐藏）"})
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    @ExcelProperty(value = {"菜单状态（0正常 1停用）"})
    private String status;

    @Schema(description = "权限标识")
    @ExcelProperty(value = {"权限标识"})
    private String perms;

    @Schema(description = "菜单图标")
    @ExcelProperty(value = {"菜单图标"})
    private String icon;

    @Schema(description = "请求方法")
    @ExcelProperty(value = {"请求方法"})
    private String requestMethod;

    @Schema(description = "请求url")
    @ExcelProperty(value = {"请求url"})
    private String requestUrl;

    @Schema(description = "创建者")
    @ExcelProperty(value = {"创建者"})
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "更新者")
    @ExcelProperty(value = {"更新者"})
    private String updateBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remark;



}
