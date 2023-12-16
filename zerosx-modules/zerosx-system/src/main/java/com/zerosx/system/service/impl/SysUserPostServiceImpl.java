package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.system.entity.SysUserPost;
import com.zerosx.system.mapper.ISysUserPostMapper;
import com.zerosx.system.service.ISysUserPostService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserPostServiceImpl extends SuperServiceImpl<ISysUserPostMapper, SysUserPost> implements ISysUserPostService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserPostIds(Long userId, List<Long> postIds, boolean deleted) {
        if (deleted) {
            LambdaQueryWrapper<SysUserPost> rmqw = Wrappers.lambdaQuery(SysUserPost.class);
            rmqw.eq(SysUserPost::getUserId, userId);
            remove(rmqw);
        }
        if (CollectionUtils.isEmpty(postIds)) {
            return true;
        }
        List<SysUserPost> surList = new ArrayList<>();
        SysUserPost sur;
        for (Long postId : postIds) {
            sur = new SysUserPost();
            sur.setUserId(userId);
            sur.setPostId(postId);
            surList.add(sur);
        }
        for (SysUserPost sysUserPost : surList) {
            save(sysUserPost);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUserPosts(Long userId) {
        LambdaQueryWrapper<SysUserPost> rmqw = Wrappers.lambdaQuery(SysUserPost.class);
        rmqw.eq(SysUserPost::getUserId, userId);
        return remove(rmqw);
    }

}
