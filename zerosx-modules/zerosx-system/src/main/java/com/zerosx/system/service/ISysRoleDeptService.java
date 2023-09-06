package com.zerosx.system.service;

import com.zerosx.common.core.service.ISuperService;
import com.zerosx.system.entity.SysRoleDept;

import java.util.List;

public interface ISysRoleDeptService extends ISuperService<SysRoleDept> {

    boolean updateSysDeptRoles(Long id, List<Long> roleIds, boolean delete);

    boolean removeByDeptIds(Long[] ids);
}
