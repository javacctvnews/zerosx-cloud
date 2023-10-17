package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-02 15:06:36
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_system_operator_log")
public class SystemOperatorLog extends SuperEntity<SystemOperatorLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 模块按钮名称
     */
    private String btnName;

    /**
     * 按钮操作类别
     */
    private String businessType;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String operatorName;

    /**
     * 请求URL
     */
    private String operatorUrl;

    /**
     * 主机地址
     */
    private String operatorIp;
    /**
     * 主机地址归属地
     */
    private String ipLocation;
    /**
     * 请求参数
     */
    private String operatorParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operatorTime;

    /**
     * 消耗时间
     */
    private Long costTime;

    //租户ID
    private String operatorId;

    /**
     * 逻辑删除，0：未删除；1：已删除
     */
    @TableLogic
    private Integer deleted;

}
