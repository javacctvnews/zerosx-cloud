package com.zerosx.common.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Description 系统登录用户
 * @Author javacctvnews
 * @Date 2023/3/13 18:20
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserBO implements Serializable {

    private static final long serialVersionUID = 1599500431316441602L;

    //自增ID
    private Long id;
    //部门Id
    private Long deptId;
    //用户名
    private String userName;
    //用户密码
    private String password;
    //用户昵称
    private String nickName;
    //用户联系号码
    private String phoneNumber;
    //UserTypeEnum 用户类型
    private String userType;
    //用户openID
    private String openId;
    //账号状态：0-正常；1-停用
    private String status;
    //功能权限集合
    private List<SysRoleBO> roles;
    //租户标识
    private String operatorId;
    //租户集团ID集合
    private Set<String> operatorIds;
    //租户集团子租户ID集合
    private Set<String> subTenantIds;

    public Boolean getEnabled() {
        return "0".equals(status);
    }

}
