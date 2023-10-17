package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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
@ExcelIgnoreUnannotated
@Schema(description = "操作日志:分页结果对象")
public class SystemOperatorLogPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志主键")
    @ExcelIgnore
    private Long id;

    @Schema(description = "模块名称")
    @ExcelProperty(value = {"模块名称"})
    private String title;

    @Schema(description = "模块按钮名称")
    @ExcelProperty(value = {"操作按钮名称"})
    private String btnName;

    @Schema(description = "操作人员")
    @ExcelProperty(value = {"操作人员"})
    private String operatorName;

    @Schema(description = "操作时间")
    @ExcelProperty(value = {"操作时间"})
    private Date operatorTime;

    @Schema(description = "消耗时间")
    @ExcelProperty(value = {"消耗时间(ms)"})
    private Long costTime;

    @Schema(description = "操作状态（0正常 1异常）")
    @ExcelProperty(value = {"操作状态（0正常 1异常）"})
    private Integer status;

    @Schema(description = "主机地址")
    @ExcelProperty(value = {"主机地址"})
    private String operatorIp;

    @Schema(description = "主机地址归属地")
    @ExcelProperty(value = {"主机地址"})
    private String ipLocation;

    @Schema(description = "请求URL")
    @ExcelProperty(value = {"请求URL"})
    private String operatorUrl;

    @Schema(description = "方法名称")
    @ExcelProperty(value = {"方法名称"})
    private String methodName;

    @Schema(description = "请求方式")
    @ExcelProperty(value = {"请求方式"})
    private String requestMethod;

    @Schema(description = "按钮操作类别")
    @ExcelProperty(value = {"按钮操作类别"})
    private String businessType;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    @ExcelIgnore
    private Integer operatorType;

    @Schema(description = "请求参数")
    @ExcelIgnore
    private String operatorParam;

    @Schema(description = "返回参数")
    @ExcelIgnore
    private String jsonResult;

    @Schema(description = "错误消息")
    @ExcelIgnore
    private String errorMsg;

    @Schema(description = "最新更新时间")
    @ExcelIgnore
    private Date updateTime;

    //租户ID
    @ExcelIgnore
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorIdName")
    private String operatorId;
    //租户ID名称
    @ExcelProperty(value = {"运营商名称"})
    private String operatorIdName;

}
