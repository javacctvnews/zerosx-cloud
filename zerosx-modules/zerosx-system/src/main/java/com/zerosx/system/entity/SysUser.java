package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.common.encrypt.anno.EncryptClass;
import com.zerosx.common.encrypt.anno.EncryptField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统用户
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 13:48:04
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sys_user")
@EncryptClass
public class SysUser extends SuperEntity<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户编码（预留）
     */
    private String userCode;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    @EncryptField
    private String email;

    /**
     * 手机号码
     */
    @EncryptField
    private String phoneNumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String deleted;
}
