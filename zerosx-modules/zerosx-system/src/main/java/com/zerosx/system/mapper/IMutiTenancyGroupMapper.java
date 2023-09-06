package com.zerosx.system.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.system.entity.MutiTenancyGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName IMutiTenancyGroupMapper
 * @Description
 * @Author javacctvnews
 * @Date 2023/3/13 10:43
 * @Version 1.0
 */
@Mapper
public interface IMutiTenancyGroupMapper extends SuperMapper<MutiTenancyGroup> {


    int selectTenancyExist(@Param("id") Long id, @Param("tenantGroupName") String tenantGroupName, @Param("socialCreditCode") String socialCreditCode);


    MutiTenancyGroup selectTenancy(String operatorId);

}
