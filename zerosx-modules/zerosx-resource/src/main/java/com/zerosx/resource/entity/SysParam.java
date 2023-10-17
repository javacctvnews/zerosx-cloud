package com.zerosx.resource.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 01:02:29
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_param")
public class SysParam extends SuperEntity<SysParam> {

    private static final long serialVersionUID = 1L;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数编码
     */
    private String paramKey;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数范围，0：全局，1：租户公司
     */
    private String paramScope;

    /**
     * 状态，0：正常，1：停用
     */
    private String status;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 逻辑删除，0：未删除，1：删除
     */
    @TableLogic
    private Integer deleted;

}
