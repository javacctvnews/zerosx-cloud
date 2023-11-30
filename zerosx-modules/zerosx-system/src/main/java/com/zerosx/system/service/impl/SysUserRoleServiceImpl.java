package com.zerosx.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.ds.constant.DSType;
import com.zerosx.system.entity.SysUserRole;
import com.zerosx.system.mapper.ISysUserRoleMapper;
import com.zerosx.system.service.ISysUserRoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserRoleServiceImpl extends SuperServiceImpl<ISysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    @DS(DSType.MASTER)
    @DSTransactional(rollbackFor = Exception.class)
    public boolean saveUserRoleIds(Long userId, List<Long> roleIds, boolean deleted) {
        if (deleted) {
            removeUserRoles(userId);
        }
        if (CollectionUtils.isEmpty(roleIds)) {
            return true;
        }
        List<SysUserRole> surList = new ArrayList<>();
        SysUserRole sur;
        for (Long roleId : roleIds) {
            sur = new SysUserRole();
            sur.setUserId(userId);
            sur.setRoleId(roleId);
            surList.add(sur);
        }
        for (SysUserRole sysUserRole : surList) {
            save(sysUserRole);
        }
        return true;
    }

    @Override
    public boolean removeUserRoles(Long userId) {
        LambdaQueryWrapper<SysUserRole> surqw = Wrappers.lambdaQuery(SysUserRole.class);
        surqw.eq(SysUserRole::getUserId, userId);
        return remove(surqw);
    }

}
