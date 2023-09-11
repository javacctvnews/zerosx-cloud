package com.zerosx.system.vo;

import com.zerosx.common.core.anno.Sensitive;
import com.zerosx.common.core.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * OSS配置
 * @Description
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
@Getter
@Setter
@Schema(description = "OSS配置VO")
public class OssSupplierVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "服务商编码")
    private String supplierType;

    @Schema(description = "服务商名称")
    private String supplierName;

    @Schema(description = "状态，0：正常；1：停用")
    private String status;

    @Schema(description = "AccessKey")
    private String accessKeyId;

    @Schema(description = "AccessSecret")
    @Sensitive(strategy = SensitiveStrategy.PASSWORD)
    private String accessKeySecret;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "所属地域")
    private String regionId;

    @Schema(description = "endpoint")
    private String endpoint;

    @Schema(description = "域名")
    private String domainAddress;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "租户标识")
    private String operatorId;

}
