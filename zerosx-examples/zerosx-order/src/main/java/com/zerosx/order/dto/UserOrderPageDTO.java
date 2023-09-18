package com.zerosx.order.dto;

import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.order.rule.IdNumEncryptRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户订单
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-12 16:21:49
 */
@Getter
@Setter
@Schema(description = "用户订单:分页查询DTO")
public class UserOrderPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "手机号码")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String phone;

    @Schema(description = "身份号码")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String idCard;

    @Schema(description = "电子邮箱")
    @EncryptField(algo = IdNumEncryptRule.class)
    private String email;

}
