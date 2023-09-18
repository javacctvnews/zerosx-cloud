package com.zerosx.system.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import com.zerosx.encrypt2.anno.EncryptClass;
import com.zerosx.encrypt2.anno.EncryptField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName MutiTenancyGroup
 * @Description 多租户集团公司
 * @Author javacctvnews
 * @Date 2023/3/13 10:37
 * @Version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_muti_tenancy_group")
@EncryptClass
public class MutiTenancyGroup extends SuperEntity<MutiTenancyGroup> {

    @ExcelProperty("租户标识")
    private String operatorId;
    @ExcelProperty("租户公司全称")
    private String tenantGroupName;
    private String tenantShortName;
    private String socialCreditCode;
    private String businessLicensePicture;
    //使用状态，0：正常；1：停用
    private String validStatus;
    //审核状态，0：待审核；1：审核通过；2：审核未通过
    private Integer auditStatus;
    private String province;
    private String city;
    private String area;
    private String street;
    private String contactName;
    @EncryptField
    private String contactMobilePhone;
    @EncryptField
    private String telephone;
    private String logPicture;
    private String remarks;
    @TableLogic
    private Integer deleted;

}
