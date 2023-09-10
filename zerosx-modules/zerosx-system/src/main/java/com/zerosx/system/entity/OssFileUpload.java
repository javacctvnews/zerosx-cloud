package com.zerosx.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zerosx.common.core.model.SuperEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@TableName(value = "t_oss_file_upload")
public class OssFileUpload extends SuperEntity<OssFileUpload> {

    //服务商ID
    private Long ossSupplierId;
    //AccessKey
    private String accessKeyId;
    //存储桶名称
    private String bucketName;

    private String ossType;

    private String originalFileName;

    private String objectName;

    private Long objectSize;

    private String objectUrl;

    private String objectViewUrl;

    //objectViewUrl的失效时刻
    private Date expirationTime;

    private String remark;

    private String operatorId;

    @TableLogic
    private Integer deleted;

}
