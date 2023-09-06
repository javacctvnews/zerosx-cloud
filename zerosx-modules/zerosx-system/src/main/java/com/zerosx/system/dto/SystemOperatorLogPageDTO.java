package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-02 15:06:36
 */
@Getter
@Setter
@Schema(description = "操作日志:分页查询DTO")
public class SystemOperatorLogPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "模块关键字")
    private String title;

    @Schema(description = "操作人员名称")
    private String operatorName;

    @Schema(description = "操作结果（0正常 1异常）")
    private Integer status;

    @Schema(description = "开始操作时间")
    private String beginOperatorTime;

    @Schema(description = "结束操作时间")
    private String endOperatorTime;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;

}
