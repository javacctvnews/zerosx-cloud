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
@TableName(value = "t_sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -1440388362847712453L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
