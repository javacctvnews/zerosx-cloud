package com.zerosx.order.vo;

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
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@Schema(description = "用户订单:分页结果对象")
public class UserOrderPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    private Integer id;

    @Schema(description = "订单号")
    @ExcelProperty(value = {"订单号"})
    private String orderNo;

    @Schema(description = "用户ID")
    @ExcelProperty(value = {"用户ID"})
    private String userId;

    @Schema(description = "物品编码")
    @ExcelProperty(value = {"物品编码"})
    private String commodityCode;

    @Schema(description = "数量")
    @ExcelProperty(value = {"数量"})
    private Integer count;

    @Schema(description = "金额")
    @ExcelProperty(value = {"金额"})
    private Double amount;

    @Schema(description = "手机号码")
    @ExcelProperty(value = {"手机号码"})
    private String phone;

    @Schema(description = "身份号码")
    @ExcelProperty(value = {"身份号码"})
    private String idCard;

    @Schema(description = "电子邮箱")
    @ExcelProperty(value = {"电子邮箱"})
    private String email;

    @Schema(description = "昵称")
    @ExcelProperty(value = {"昵称"})
    private String nickName;

    @Schema(description = "状态")
    @ExcelProperty(value = {"状态"})
    private String status;

    @Schema(description = "创建时间")
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;

    @Schema(description = "创建人")
    @ExcelProperty(value = {"创建人"})
    private String createBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = {"更新时间"})
    private Date updateTime;

    @Schema(description = "更新人")
    @ExcelProperty(value = {"更新人"})
    private String updateBy;

    @Schema(description = "租户标识")
    @Trans(type = TranslConstants.OPERATOR, ref = "operatorName")
    private String operatorId;

    @Schema(description = "租户公司")
    @ExcelProperty(value = {"租户公司"})
    private String operatorName;
}
