package com.zerosx.system.service;

import com.zerosx.common.core.service.ISuperService;
import com.zerosx.system.entity.SysUserRole;

import java.util.List;

public interface ISysUserRoleService extends ISuperService<SysUserRole> {
    /**
     * 保存用户和角色的关系
     * @param userId 用户id
     * @param roleIds 角色ids
     * @param deleted 是否删除userId的角色再重新保存
     * @return
     */
    boolean saveUserRoleIds(Long userId, List<Long> roleIds, boolean deleted);

    /**
     * 删除用户的角色关系表
     * @param userId
     * @return
     */
    boolean removeUserRoles(Long userId);
}
