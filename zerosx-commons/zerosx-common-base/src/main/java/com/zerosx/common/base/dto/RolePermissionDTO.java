package com.zerosx.common.base.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RolePermissionDTO
 * @Description 角色权限查询
 * @Author javacctvnews
 * @Date 2023/4/11 13:09
 * @Version 1.0
 */
@Setter
@Getter
public class RolePermissionDTO implements Serializable {

    private static final long serialVersionUID = -7552867384554335962L;

    /**
     * 角色ID集合
     */
    private List<Long> roles;

    private Long userId;

    private String userName;

}
