package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色和菜单关联 sys_role_menu
 */

@Setter
@Getter
@NoArgsConstructor
@TableName(value = "t_sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 6648565544147666447L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
