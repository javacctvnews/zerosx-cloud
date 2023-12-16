package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.system.entity.SysRoleDept;
import com.zerosx.system.mapper.ISysRoleDeptMapper;
import com.zerosx.system.service.ISysRoleDeptService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SysRoleDeptServiceImpl extends SuperServiceImpl<ISysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysDeptRoles(Long deptId, List<Long> roleIds, boolean delete) {
        if (delete) {
            LambdaQueryWrapper<SysRoleDept> rm = Wrappers.lambdaQuery(SysRoleDept.class);
            rm.eq(SysRoleDept::getDeptId, deptId);
            remove(rm);
        }
        if(CollectionUtils.isEmpty(roleIds)){
            return true;
        }
        List<SysRoleDept> srdList = new ArrayList<>();
        SysRoleDept srd;
        for (Long roleId : roleIds) {
            srd = new SysRoleDept();
            srd.setDeptId(deptId);
            srd.setRoleId(roleId);
            srdList.add(srd);
        }
        for (SysRoleDept sysRoleDept : srdList) {
            save(sysRoleDept);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByDeptIds(Long[] deptIds) {
        LambdaQueryWrapper<SysRoleDept> rm = Wrappers.lambdaQuery(SysRoleDept.class);
        rm.in(SysRoleDept::getDeptId, Arrays.asList(deptIds));
        return remove(rm);
    }

}
