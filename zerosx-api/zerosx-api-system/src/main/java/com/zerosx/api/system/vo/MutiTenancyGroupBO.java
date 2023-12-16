package com.zerosx.api.system.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MutiTenancyGroupBO {

    private String operatorId;

    private String tenantGroupName;

    private String tenantShortName;

    private String validStatus;

}
