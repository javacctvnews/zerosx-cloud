package com.zerosx.sas.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Schema(description = "TokenVO")
public class TokenVO implements Serializable {

    @ExcelProperty(value = {"客户端"})
    @Schema(description = "客户端")
    private String clientId;

    @ExcelProperty(value = {"用户名"})
    @Schema(description = "用户名")
    private String username;

    @ExcelProperty(value = {"鉴权用户类型"})
    @Schema(description = "鉴权用户类型")
    private String authUserType;

    @ExcelProperty(value = {"授权类型"})
    @Schema(description = "授权类型")
    private String grantType;

    @Schema(description = "token的值")
    @ExcelProperty(value = {"TOKEN值"})
    private String tokenValue;

    @ExcelProperty(value = {"有效时长(小时)"})
    @Schema(description = "有效时长(小时)")
    private BigDecimal expirationLength;

    @ExcelProperty(value = {"登录时间"})
    @Schema(description = "登录时间")
    private Date loginExpiration;

    @ExcelProperty(value = {"到期时间"})
    @Schema(description = "到期时间")
    private Date expiration;

    @ExcelProperty(value = {"租户标识"})
    @Schema(description = "租户公司")
    private String operatorName;

    @ExcelIgnore
    @Schema(description = "租户标识")
    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;

}
