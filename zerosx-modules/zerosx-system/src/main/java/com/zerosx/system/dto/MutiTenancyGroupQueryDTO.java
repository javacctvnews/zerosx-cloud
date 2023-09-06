package com.zerosx.system.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName MutiTenancyGroupQueryVO
 * @Description 租户集团查询
 * @Author javacctvnews
 * @Date 2023/3/13 13:06
 * @Version 1.0
 */
@Setter
@Getter
public class MutiTenancyGroupQueryDTO {

    private String operatorId;

    private String tenantGroupName;

    private String tenantShortName;

    private String socialCreditCode;

    private String validStatus;

    private String province;

    private String city;

    private String area;

    private String contactMobilePhone;

    private String contactName;

}
