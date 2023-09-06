package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysPostDTO;
import com.zerosx.system.dto.SysPostPageDTO;
import com.zerosx.system.entity.SysPost;
import com.zerosx.system.mapper.ISysPostMapper;
import com.zerosx.system.service.ISysPostService;
import com.zerosx.system.vo.SysPostPageVO;
import com.zerosx.system.vo.SysPostVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-28 15:40:01
 */
@Slf4j
@Service
public class SysPostServiceImpl extends SuperServiceImpl<ISysPostMapper, SysPost> implements ISysPostService {

    @Override
    public CustomPageVO<SysPostPageVO> pageList(RequestVO<SysPostPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SysPostPageVO.class);
    }

    @Override
    public List<SysPost> dataList(SysPostPageDTO query) {
        return list(getWrapper(query));
    }

    private static LambdaQueryWrapper<SysPost> getWrapper(SysPostPageDTO query) {
        LambdaQueryWrapper<SysPost> listqw = Wrappers.lambdaQuery(SysPost.class);
        if (query == null) {
            return listqw;
        }
        listqw.eq(StringUtils.isNotBlank(query.getOperatorId()), SysPost::getOperatorId, query.getOperatorId());
        listqw.like(StringUtils.isNotBlank(query.getPostName()), SysPost::getPostName, query.getPostName());
        listqw.eq(StringUtils.isNotBlank(query.getStatus()), SysPost::getStatus, query.getStatus());
        listqw.orderByAsc(SysPost::getPostSort);
        listqw.orderByDesc(SysPost::getCreateTime);
        return listqw;
    }

    @Override
    public boolean add(SysPostDTO sysPostDTO) {
        SysPost addEntity = BeanCopierUtil.copyProperties(sysPostDTO, SysPost.class);
        addEntity.setPostCode(IdGenerator.getIdStr());
        return save(addEntity);
    }

    @Override
    public boolean update(SysPostDTO sysPostDTO) {
        SysPost dbUpdate = getById(sysPostDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SysPost updateEntity = BeanCopierUtil.copyProperties(sysPostDTO, SysPost.class);
        return updateById(updateEntity);
    }

    @Override
    public SysPostVO queryById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), SysPostVO.class);
    }

    @Override
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public List<SysPost> queryUserPosts(Long userId) {
        return baseMapper.queryUserPosts(userId);
    }
}
