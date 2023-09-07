package com.zerosx.system.vo;

import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 * @Description
 * @author javacctvnews
 * @date 2023-07-20 13:48:04
 */
@Getter
@Setter
@Schema(description = "系统用户VO")
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型（sys_user系统用户）")
    private String userType;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    @Trans(type = CommonConstants.TRANS_OSS, ref = "avatar")
    private String avatar;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "帐号状态（0正常 1停用）")
    private String status;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "角色Ids")
    private List<Long> roleIds;

    @Schema(description = "岗位Ids")
    private List<Long> postIds;

}
