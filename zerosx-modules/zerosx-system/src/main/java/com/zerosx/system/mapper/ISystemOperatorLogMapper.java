package com.zerosx.system.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.system.dto.SystemOperatorLogPageDTO;
import com.zerosx.system.entity.SystemOperatorLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @date 2023-04-02 15:06:36
 */
@Mapper
public interface ISystemOperatorLogMapper extends SuperMapper<SystemOperatorLog> {

    long queryExportCount(SystemOperatorLogPageDTO systemOperatorLogPageDTO);

    List<SystemOperatorLog> queryExportPage(SystemOperatorLogPageDTO systemOperatorLogPageDTO);
}
