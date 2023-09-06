package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色信息表
 * @Description
 * @author javacctvnews
 * @date 2023-07-27 17:53:15
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_role")
public class SysRole extends SuperEntity<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    private Integer deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
    
    /**
     * 备注
     */
    private String remark;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private Integer deleted;

}
