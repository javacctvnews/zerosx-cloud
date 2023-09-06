package com.zerosx.system.mapper;

import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表
 *
 * @author javacctvnews
 * @date 2023-07-20 14:49:30
 */
@Mapper
public interface ISysMenuMapper extends SuperMapper<SysMenu> {

    List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<SysMenu> selectMenuListByUserId(@Param("roleIds")Set<Long> roleIds);

    List<Long> queryMenuListByRoleId(Long roleId);

    List<SysMenuBO> findByRoleCodes(@Param("roleIds")List<Long> roleIds);

    List<String> queryPermList(@Param("roles") Set<Long> roles);

    List<SysMenu> selectMenuTreeByRoleIds(@Param("roleIds")Set<Long> roleIds);
}
