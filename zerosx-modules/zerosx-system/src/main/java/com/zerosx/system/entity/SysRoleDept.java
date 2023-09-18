package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户与角色关联 t_sys_user_role
 */

@Setter
@Getter
@NoArgsConstructor
@TableName(value = "t_sys_role_dept")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 6798411544311985077L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;

}
