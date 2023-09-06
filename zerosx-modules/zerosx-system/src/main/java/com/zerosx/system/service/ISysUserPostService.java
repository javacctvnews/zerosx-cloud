package com.zerosx.system.service;

import com.zerosx.common.core.service.ISuperService;
import com.zerosx.system.entity.SysUserPost;

import java.util.List;

public interface ISysUserPostService extends ISuperService<SysUserPost> {

    /**
     * 保存用户和岗位的关系
     * @param userId 用户id
     * @param postIds 岗位ids
     * @param deleted 是否删除userId的岗位再重新保存
     * @return
     */
    boolean saveUserPostIds(Long userId, List<Long> postIds, boolean deleted);

    boolean removeUserPosts(Long userId);

}
