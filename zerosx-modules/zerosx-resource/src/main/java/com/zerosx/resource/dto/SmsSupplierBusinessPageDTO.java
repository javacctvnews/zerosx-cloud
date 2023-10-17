package com.zerosx.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.io.Serializable;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
@Getter
@Setter
@Schema(description = "短信业务模板:分页查询DTO")
public class SmsSupplierBusinessPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="服务商ID")
    private Long smsSupplierId;

}
