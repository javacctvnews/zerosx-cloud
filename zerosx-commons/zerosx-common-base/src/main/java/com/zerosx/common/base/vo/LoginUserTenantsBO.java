package com.zerosx.common.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @ClassName LoginUserTenantsBO
 * @Description 当前登录用户的租户ID和子租户ID集合
 * @Author javacctvnews
 * @Date 2023/3/14 14:38
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserTenantsBO implements Serializable {

    private static final long serialVersionUID = 4266985533178802163L;

    //登录用户ID
    private Long userId;
    //登录用户名
    private String username;
    //用户类型
    private String userType;
    //租户标识
    private String operatorId;
    //租户ID集合-预留
    private Set<String> operatorIds;
    //子租户ID集合-预留
    private Set<String> subTenantIds;
    //角色集合
    private Set<Long> roleIds;
    //权限
    private List<SysMenuBO> perms;
    //权限，格式是：[请求方式:请求URL]
    private Set<String> permsSet;

}
