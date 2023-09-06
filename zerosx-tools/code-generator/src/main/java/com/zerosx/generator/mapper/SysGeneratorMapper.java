package com.zerosx.generator.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerosx.common.core.service.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Mapper
public interface SysGeneratorMapper extends SuperMapper {

    List<Map<String, Object>> queryList(Page<Map<String, Object>> page, @Param("p") Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
