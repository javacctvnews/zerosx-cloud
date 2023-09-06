package com.zerosx.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName MutiTenancyGroupSaveDTO
 * @Description 租户集团公司 保存对象
 * @Author javacctvnews
 * @Date 2023/3/13 10:53
 * @Version 1.0
 */
@Setter
@Getter
@Schema(description = "租户集团公司 保存对象")
public class MutiTenancyGroupSaveDTO {

    @NotBlank(message = "公司名称全称为空")
    @Schema(description = "公司全称",required = true)
    private String tenantGroupName;

    @Schema(description = "公司简称")
    private String tenantShortName;

    @Schema(description = "状态")
    private String validStatus;

    @NotBlank(message = "社会信用代码为空")
    @Schema(description = "统一社会信用代码", required = true)
    private String socialCreditCode;

    @NotBlank(message = "营业执照为空")
    @Schema(description = "营业执照", required = true)
    private String businessLicensePicture;

    @NotBlank(message = "公司所在省为空")
    @Schema(description = "公司所在省", required = true)
    private String province;
    @NotBlank(message = "公司所在市为空")
    @Schema(description = "公司所在市", required = true)
    private String city;
    @Schema(description = "公司所在区")
    private String area;
    @NotBlank(message = "公司详细街道为空")
    @Schema(description = "详细街道地址", required = true)
    private String street;

    @NotBlank(message = "公司联系人为空")
    @Schema(description = "联系人姓名", required = true)
    private String contactName;

    @NotBlank(message = "公司联系人手机号码")
    @Schema(description = "联系人手机号码", required = true)
    private String contactMobilePhone;
    @Schema(description = "公司电话")
    private String telephone;
    @Schema(description = "公司Logo")
    private String logPicture;
    @Schema(description = "备注")
    private String remarks;

}
