package com.zerosx.system.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.system.entity.SystemOperatorLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 * 
 * @author javacctvnews
 * @date 2023-04-02 15:06:36
 */
@Mapper
public interface ISystemOperatorLogMapper extends SuperMapper<SystemOperatorLog> {

}
