package com.zerosx.resource.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.resource.entity.AreaCitySource;
import com.zerosx.resource.vo.AreaCitySourceTreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 行政区域数据源
 * 
 * @author javacctvnews
 * @date 2023-04-12 13:48:43
 */
@Mapper
public interface IAreaCitySourceMapper extends SuperMapper<AreaCitySource> {

    List<AreaCitySourceTreeVO> lazyTreeData(String parentCode);

}
