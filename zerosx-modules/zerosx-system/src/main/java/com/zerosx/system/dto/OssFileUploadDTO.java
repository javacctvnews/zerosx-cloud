package com.zerosx.system.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OssFileUploadDTO {

    private String ossType;

    private String fileName;

    private String operatorId;

    private String bucketName;

    //服务商ID
    private Long ossSupplierId;
}
