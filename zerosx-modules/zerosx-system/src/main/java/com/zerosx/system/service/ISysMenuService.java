package com.zerosx.system.service;

import com.zerosx.common.base.dto.RolePermissionDTO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SysMenuBO;
import com.zerosx.common.base.vo.SysPermissionBO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysMenuDTO;
import com.zerosx.system.dto.SysMenuPageDTO;
import com.zerosx.system.dto.SysRoleMenuQueryDTO;
import com.zerosx.system.entity.SysMenu;
import com.zerosx.system.vo.RouterVO;
import com.zerosx.system.vo.SysMenuPageVO;
import com.zerosx.system.vo.SysRoleMenuTreeVO;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 14:49:30
 */
public interface ISysMenuService extends ISuperService<SysMenu> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysMenuPageVO> pageList(RequestVO<SysMenuPageDTO> requestVO, boolean searchCount);

    /**
     * 新增
     *
     * @param sysMenuDTO
     * @return
     */
    boolean add(SysMenuDTO sysMenuDTO);

    /**
     * 编辑
     *
     * @param sysMenuDTO
     * @return
     */
    boolean update(SysMenuDTO sysMenuDTO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteRecord(Long id);

    List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<RouterVO> buildMenus(List<SysMenu> menus);

    List<SysMenu> selectMenuList(SysMenuPageDTO menu);

    SysMenu getMenuById(Long menuId);

    SysRoleMenuTreeVO roleMenuTree(SysRoleMenuQueryDTO sysRoleMenuQueryDTO);

    SysPermissionBO findByRoleCodes(RolePermissionDTO rolePermissionDTO);

    List<SysMenuBO> findByRoleCodes(List<Long> roleIds);

    Set<String> queryPermList(Set<Long> roles);
}

