package com.zerosx.resource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统参数
 * @Description
 * @author javacctvnews
 * @date 2023-07-29 01:02:29
 */
@Getter
@Setter
@Schema(description = "系统参数VO")
public class SysParamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "参数名")
    private String paramName;

    @Schema(description = "参数编码")
    private String paramKey;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "参数范围，0：全局，1：租户")
    private String paramScope;

    @Schema(description = "状态，0：正常，1：停用")
    private String status;

    @Schema(description = "备注说明")
    private String remark;

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
