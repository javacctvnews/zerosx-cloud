package com.zerosx.resource.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 短信配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-30 18:28:13
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_sms_supplier")
public class SmsSupplier extends SuperEntity<SmsSupplier> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务商编码
     */
    private String supplierType;

    /**
     * 服务商名称
     */
    private String supplierName;

    /**
     * 状态，0：正常；1：停用
     */
    private String status;

    /**
     * Access Key
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signature;

    /**
     * regionId
     */
    private String regionId;

    /**
     * 租户标识
     */
    private String operatorId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * sms请求地址
     */
    private String domainAddress;

    /**
     * key值
     */
    private String keyValue;

    /**
     * 逻辑删除，0：未删除；1：已删除
     */
    @TableLogic
    private Integer deleted;

}
