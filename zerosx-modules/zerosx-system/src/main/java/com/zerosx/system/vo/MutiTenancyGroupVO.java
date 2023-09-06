package com.zerosx.system.vo;

import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MutiTenancyGroupVO {

    private Long id;

    private String operatorId;

    private String tenantGroupName;

    private String tenantShortName;

    private String socialCreditCode;
    @Trans(type = CommonConstants.TRANS_OSS, ref = "businessLicensePictureUrl")
    private String businessLicensePicture;
    private String businessLicensePictureUrl;
    @Trans(type = CommonConstants.TRANS_DICT, key = "StatusEnum", ref = "validStatusMsg")
    private String validStatus;
    private String validStatusMsg;

    private Integer auditStatus;
    private String auditStatusMsg;

    @Trans(type = CommonConstants.TRANS_REGION, ref = "provinceName")
    private String province;
    private String provinceName;
    @Trans(type = CommonConstants.TRANS_REGION, ref = "cityName")
    private String city;
    private String cityName;
    @Trans(type = CommonConstants.TRANS_REGION, ref = "areaName")
    private String area;
    private String areaName;
    private String street;
    private String contactName;
    private String contactMobilePhone;
    private String telephone;

    @Trans(type = CommonConstants.TRANS_OSS, ref = "logPictureUrl")
    private String logPicture;
    private String logPictureUrl;

    private String remarks;

    private Date createTime;

    private Date updateTime;
}
