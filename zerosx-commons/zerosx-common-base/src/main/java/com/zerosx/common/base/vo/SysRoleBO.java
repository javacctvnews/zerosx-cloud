package com.zerosx.common.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class SysRoleBO implements Serializable {

    private static final long serialVersionUID = 4497149010220586111L;
    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;
}
