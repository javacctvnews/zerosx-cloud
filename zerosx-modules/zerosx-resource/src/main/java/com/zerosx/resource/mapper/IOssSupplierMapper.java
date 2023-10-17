package com.zerosx.resource.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.resource.dto.OssSupplierPageDTO;
import com.zerosx.resource.entity.OssSupplier;
import org.apache.ibatis.annotations.Mapper;

/**
 * OSS配置
 * 
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
@Mapper
public interface IOssSupplierMapper extends SuperMapper<OssSupplier> {

    OssSupplier selectOssSupplier(OssSupplierPageDTO pageDTO);
}
