package com.zerosx.system.mapper;

import com.zerosx.system.entity.SysRole;
import com.zerosx.system.vo.SysRoleVO;
import com.zerosx.common.core.service.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色信息表
 * 
 * @author javacctvnews
 * @date 2023-07-27 17:53:15
 */
@Mapper
public interface ISysRoleMapper extends SuperMapper<SysRole> {

    List<SysRoleVO> selectRoleByUserId(Long userId);

}
