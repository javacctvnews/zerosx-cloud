package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.core.anno.Sensitive;
import com.zerosx.common.core.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 * @Description
 * @author javacctvnews
 * @date 2023-07-20 13:48:04
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "系统用户:分页结果对象")
public class SysUserPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @ExcelIgnore
    private Long id;

    @Schema(description = "部门ID")
    @ExcelIgnore
    private Long deptId;

    @Schema(description = "所属部门")
    @ExcelProperty(value = {"所属部门"})
    private String deptName;

    @Schema(description = "用户账号")
    @ExcelProperty(value = {"用户账号"})
    private String userName;

    @Schema(description = "用户昵称")
    @ExcelProperty(value = {"用户昵称"})
    private String nickName;

    @Schema(description = "用户类型（sys_user系统用户）")
    @ExcelProperty(value = {"用户类型（sys_user系统用户）"})
    private String userType;

    @Schema(description = "用户邮箱")
    @ExcelProperty(value = {"用户邮箱"})
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;

    @Schema(description = "手机号码")
    @ExcelProperty(value = {"手机号码"})
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phoneNumber;

    @Schema(description = "用户性别（0男 1女 2未知）")
    @ExcelProperty(value = {"用户性别（0男 1女 2未知）"})
    @Trans(type = CommonConstants.TRANS_DICT, key = "SexEnum", ref = "sex")
    private String sex;

    @Schema(description = "头像地址")
    @ExcelIgnore
    private String avatar;

    @Schema(description = "密码")
    @ExcelIgnore
    private String password;

    @Schema(description = "帐号状态（0正常 1停用）")
    @ExcelProperty(value = {"帐号状态（0正常 1停用）"})
    private String status;

    @Schema(description = "最后登录IP")
    @ExcelProperty(value = {"最后登录IP"})
    private String loginIp;

    @Schema(description = "最后登录时间",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ExcelProperty(value = {"最后登录时间"})
    private Date loginDate;

    @Schema(description = "创建者")
    @ExcelProperty(value = {"创建者"})
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "更新者")
    @ExcelProperty(value = {"更新者"})
    private String updateBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "备注")
    @ExcelProperty(value = {"备注"})
    private String remark;

    @Schema(description = "租户标识")
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;

}
