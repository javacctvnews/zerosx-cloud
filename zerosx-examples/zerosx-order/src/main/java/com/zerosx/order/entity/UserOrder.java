package com.zerosx.order.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.core.encryptor.AesEncryptor;
import com.zerosx.order.rule.IdNumEncryptRule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户订单
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-12 16:21:49
 */
@EncryptClass
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_order")
public class UserOrder extends SuperEntity<UserOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 物品编码
     */
    private String commodityCode;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 手机号码
     */
    @EncryptField(algo = AesEncryptor.class)
    private String phone;

    /**
     * 身份号码
     */
    @EncryptField(algo = IdNumEncryptRule.class)
    private String idCard;

    /**
     * 电子邮箱
     */
    @EncryptField(algo = IdNumEncryptRule.class)
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 状态
     */
    private String status;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

}
