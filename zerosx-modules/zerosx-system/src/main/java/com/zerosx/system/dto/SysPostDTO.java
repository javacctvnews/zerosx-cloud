package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * 岗位管理
 * @Description
 * @author javacctvnews
 * @date 2023-07-28 15:40:01
 */
@Getter
@Setter
@Schema(description = "岗位管理DTO")
public class SysPostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "岗位ID")
    private Long id;

    @Schema(description = "租户标识")
    private String operatorId;

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "显示顺序")
    private Integer postSort;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "备注")
    private String remark;

}
