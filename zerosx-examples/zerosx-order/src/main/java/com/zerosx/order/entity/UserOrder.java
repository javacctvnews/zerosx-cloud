package com.zerosx.order.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户订单
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 14:09:54
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_order")
@EncryptClass   //字段需要加解密
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
    @EncryptField
    private String phone;

    /**
     * 身份号码
     */
    @EncryptField
    private String idCard;

    /**
     * 电子邮箱
     */
    @EncryptField
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
