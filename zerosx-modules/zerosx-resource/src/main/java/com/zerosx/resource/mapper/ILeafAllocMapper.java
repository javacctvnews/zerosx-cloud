package com.zerosx.resource.mapper;


import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.resource.entity.LeafAlloc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILeafAllocMapper extends SuperMapper<LeafAlloc> {

     int updateMaxIdAndGetLeafAlloc(String tag);

     int updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

}
