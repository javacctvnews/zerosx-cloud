package com.zerosx.common.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * BaseDTO
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-26 20:51
 **/
@Data
@Schema(description = "租户基础DTO")
public class BaseTenantDTO {

    @Schema(description = "租户标识")
    private String operatorId;

}
