package com.zerosx.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.ds.constant.DSType;
import com.zerosx.system.entity.SysRoleMenu;
import com.zerosx.system.mapper.ISysRoleMenuMapper;
import com.zerosx.system.service.ISysRoleMenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends SuperServiceImpl<ISysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    @DSTransactional
    @DS(DSType.MASTER)
    public boolean saveSysRoleMenus(List<SysRoleMenu> sysRoleMenus) {
        if (CollectionUtils.isEmpty(sysRoleMenus)) {
            return true;
        }
        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
            save(sysRoleMenu);
        }
        return true;
    }
}
