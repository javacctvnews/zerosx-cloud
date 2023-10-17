package com.zerosx.resource.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.resource.entity.OssFileUpload;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOssFileUploadMapper extends SuperMapper<OssFileUpload> {

    OssFileUpload selectByObjectName(String objectName);

}
