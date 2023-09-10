package com.zerosx.system.vo;

import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.base.constants.CommonConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class OssFileUploadPageVO {

    private Long id;

    private String ossType;

    private String originalFileName;

    private String objectName;

    private Long objectSize;

    private String objectSizeStr;

    private String objectUrl;

    private String objectViewUrl;

    //objectViewUrl的失效时刻
    private Date expirationTime;

    private String remark;

    private Date createTime;

    private Date updateTime;

    @Trans(type = CommonConstants.TRANS_OPERATOR_ID, ref = "operatorName")
    private String operatorId;
    //租户ID名称
    private String operatorName;

    private String createBy;

    private String updateBy;

    //服务商ID
    private Long ossSupplierId;
    //AccessKey
    private String accessKeyId;
    //存储桶名称
    private String bucketName;

}
