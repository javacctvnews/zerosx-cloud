package com.zerosx.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


/**
 * @ClassName MutiTenancyGroupEditDTO
 * @Description 集团公司更新
 * @Author javacctvnews
 * @Date 2023/3/13 11:36
 * @Version 1.0
 */
@Setter
@Getter
public class MutiTenancyGroupEditDTO {

    @NotNull(message = "租户集团ID为空")
    private Long id;

    private String operatorId;

    private String tenantGroupName;

    private String tenantShortName;

    private String socialCreditCode;

    private String businessLicensePicture;

    private String validStatus;

    private String province;

    private String city;

    private String area;

    private String street;

    private String contactName;

    private String contactMobilePhone;

    private String telephone;

    private String logPicture;

    private String remarks;

}
