package com.zerosx.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.core.anno.Sensitive;
import com.zerosx.common.core.enums.SensitiveStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName MutiTenancyGroupPageVO
 * @Description 租户集团分页
 * @Author javacctvnews
 * @Date 2023/3/13 12:57
 * @Version 1.0
 */
@Setter
@Getter
public class MutiTenancyGroupPageVO {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = {"租户标识"})
    private String operatorId;

    @ExcelProperty(value = {"公司全称"})
    private String tenantGroupName;

    @ExcelProperty(value = {"公司简称"})
    private String tenantShortName;

    @ExcelProperty(value = {"社会信用代码"})
    private String socialCreditCode;

    @ExcelIgnore
    @Trans(type = TranslConstants.OSS, ref = "businessLicensePictureUrl")
    private String businessLicensePicture;
    @ExcelProperty(value = {"营业执照"})
    private String businessLicensePictureUrl;

    @ExcelProperty(value = {"状态"})
    @Trans(type = TranslConstants.DICT, key = "StatusEnum", ref = "validStatusMsg")
    private String validStatus;
    @ExcelIgnore
    private String validStatusMsg;
    @ExcelIgnore
    private Integer auditStatus;
    @ExcelIgnore
    private String auditStatusMsg;
    @ExcelIgnore
    @Trans(type = TranslConstants.REGION, ref = "provinceName")
    private String province;
    @ExcelProperty(value = {"所在省"})
    private String provinceName;
    @ExcelIgnore
    @Trans(type = TranslConstants.REGION, ref = "cityName")
    private String city;
    @ExcelProperty(value = {"所在市"})
    private String cityName;
    @ExcelIgnore
    @Trans(type = TranslConstants.REGION, ref = "areaName")
    private String area;
    @ExcelProperty(value = {"所在区"})
    private String areaName;
    @ExcelProperty(value = {"详细街道"})
    private String street;
    @ExcelProperty(value = {"联系人姓名"})
    private String contactName;
    @ExcelProperty(value = {"联系人电话"})
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String contactMobilePhone;
    @ExcelProperty(value = {"公司电话"})
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String telephone;
    @ExcelIgnore
    @Trans(type = TranslConstants.OSS, ref = "logPictureUrl")
    private String logPicture;
    @ExcelProperty(value = {"Logo"})
    private String logPictureUrl;
    @ExcelProperty(value = {"备注"})
    private String remarks;
    @ExcelProperty(value = {"创建时间"})
    private Date createTime;
    @ExcelIgnore
    private Date updateTime;

}
