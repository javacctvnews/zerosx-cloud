package com.zerosx.sas.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.TranslConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-09 15:33:32
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "登录日志:分页结果对象")
public class OauthTokenRecordPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    @ExcelIgnore
    private Long id;

    @Schema(description = "用户名")
    @ExcelProperty(value = {"用户名"})
    private String username;

    @Schema(description = "token值")
    @ExcelProperty(value = {"token值"})
    private String tokenValue;

    @Schema(description = "申请授权时间")
    @ExcelProperty(value = {"申请授权时间"})
    private Date applyOauthTime;

    @Schema(description = "登录IP")
    @ExcelProperty(value = {"登录IP"})
    private String sourceIp;

    @Schema(description = "登录地点")
    @ExcelProperty(value = {"登录地点"})
    private String sourceLocation;

    @Schema(description = "浏览器")
    @ExcelProperty(value = {"浏览器"})
    private String browserType;

    @Schema(description = "操作系统")
    @ExcelProperty(value = {"操作系统"})
    private String osType;

    @Schema(description = "授权结果，0：成功，1：失败")
    @ExcelProperty(value = {"授权结果，0：成功，1：失败"})
    private Integer oauthResult;

    @Schema(description = "授权结果描述")
    @ExcelProperty(value = {"授权结果描述"})
    private String oauthMsg;

    @Schema(description = "客户端类型")
    @ExcelProperty(value = {"客户端类型"})
    private String clientId;

    @Schema(description = "授权模式")
    @ExcelProperty(value = {"授权模式"})
    private String grantType;

    @Schema(description = "申请ID")
    @ExcelProperty(value = {"申请ID"})
    private String requestId;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @ExcelIgnore
    @Schema(description = "租户标识")
    @Trans(type = TranslConstants.OPERATOR, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
