package com.zerosx.system.entity;

import com.zerosx.common.core.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sms_supplier_business")
public class SmsSupplierBusiness extends SuperEntity<SmsSupplierBusiness> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务商ID
     */
    private Long smsSupplierId;

    /**
     * 短信业务编码
     */
    private String businessCode;

    /**
     * 短信模板编号
     */
    private String templateCode;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 模板签名
     */
    private String signature;

    /**
     * 状态，0：正常；1：停用
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 逻辑删除，0：未删除；1：已删除
     */
    @TableLogic
    private Integer deleted;

}
