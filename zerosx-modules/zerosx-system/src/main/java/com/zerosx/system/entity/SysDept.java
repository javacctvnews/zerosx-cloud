package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 部门表
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 17:42:27
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_dept")
@EncryptClass
public class SysDept extends SuperEntity<SysDept> {

    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 父部门id
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    @EncryptField
    private String phone;

    /**
     * 邮箱
     */
    @EncryptField
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    private String status;

    /**
     * 租户标识
     */
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

    @TableField(exist = false)
    private String operatorName;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 下级部门
     */
    @TableField(exist = false)
    private List<SysDept> children;

}
