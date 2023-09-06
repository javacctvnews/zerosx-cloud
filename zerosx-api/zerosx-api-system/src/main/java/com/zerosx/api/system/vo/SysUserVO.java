package com.zerosx.api.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 1074922447370175920L;

    @Schema(description = "系统用户ID")
    private Long userId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "用户类型")
    private String userType;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别：1-男；2-女；3-保密")
    private Integer sex;

    @Schema(description = "账号状态：1-正常；0-停用")
    private Integer status;

    @Schema(description = "最近登录时间")
    private Date loginTime;

    @Schema(description = "创建人")
    private String createUser;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "角色名s")
    private String roleNames;

    @Schema(description = "角色ids")
    private String roleIds;

    @Schema(description = "角色ids")
    private List<Integer> roleIdList;

    public List<Integer> getRoleIdList() {
        return StringUtils.isNotBlank(roleIds) ?
                Arrays.stream(roleIds.split(",")).map(Integer::parseInt).collect(Collectors.toList()) : new ArrayList<>();
    }

}