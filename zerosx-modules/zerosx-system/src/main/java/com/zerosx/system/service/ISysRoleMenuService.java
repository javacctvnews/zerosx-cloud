package com.zerosx.system.service;

import com.zerosx.common.core.service.ISuperService;
import com.zerosx.system.entity.SysRoleMenu;

import java.util.List;

public interface ISysRoleMenuService extends ISuperService<SysRoleMenu> {

    boolean saveSysRoleMenus(List<SysRoleMenu> sysRoleMenus);

    boolean removeByMenuId(Long menuId);
}
