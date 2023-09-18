package com.zerosx.order.vo;

import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.order.rule.IdNumEncryptRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-12 16:21:49
 */
@EncryptClass
@Getter
@Setter
@Schema(description = "用户订单VO")
public class UserOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增ID")
    private Integer id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "物品编码")
    private String commodityCode;

    @Schema(description = "数量")
    private Integer count;

    @Schema(description = "金额")
    private Double amount;

    @Schema(description = "手机号码")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String phone;

    @Schema(description = "身份号码")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String idCard;

    @Schema(description = "电子邮箱")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String email;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "租户标识")
    private String operatorId;

}
