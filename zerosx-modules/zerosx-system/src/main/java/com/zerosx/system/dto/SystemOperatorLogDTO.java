package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * @Description
 * @author javacctvnews
 * @date 2023-04-02 15:06:36
 */
@Getter
@Setter
@Schema(description = "操作日志DTO")
public class SystemOperatorLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志主键")
    private Long id;

    @Schema(description = "模块标题")
    private String title;

    @Schema(description = "模块按钮名称")
    private String btnName;

    @Schema(description = "按钮操作类别")
    private String businessType;

    @Schema(description = "方法名称")
    private String methodName;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    @Schema(description = "操作人员")
    private String operatorName;

    @Schema(description = "请求URL")
    private String operatorUrl;

    @Schema(description = "主机地址")
    private String operatorIp;

    @Schema(description = "主机地址归属地")
    private String ipLocation;

    @Schema(description = "请求参数")
    private String operatorParam;

    @Schema(description = "返回参数")
    private String jsonResult;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private Date operatorTime;

    @Schema(description = "消耗时间")
    private Long costTime;
    //租户标识
    private String operatorId;

    @Schema(description = "请求标识")
    private Long requestId;
}
