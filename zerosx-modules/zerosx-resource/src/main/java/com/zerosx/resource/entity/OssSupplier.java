package com.zerosx.resource.entity;

import com.zerosx.common.core.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * OSS配置
 * @Description
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("t_oss_supplier")
public class OssSupplier extends SuperEntity<OssSupplier> {

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
     * AccessKey
     */
    private String accessKeyId;

    /**
     * AccessSecret
     */
    private String accessKeySecret;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 所属地域
     */
    private String regionId;

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * 域名
     */
    private String domainAddress;

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
