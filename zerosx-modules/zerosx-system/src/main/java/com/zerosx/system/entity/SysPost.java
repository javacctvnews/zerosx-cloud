package com.zerosx.system.entity;

import com.zerosx.common.core.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * 岗位管理
 * @Description
 * @author javacctvnews
 * @date 2023-07-28 15:40:01
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_post")
public class SysPost extends SuperEntity<SysPost> {

    private static final long serialVersionUID = 1L;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 显示顺序
     */
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除，0：未删除，1：删除
     */
    @TableLogic
    private Integer deleted;

}
