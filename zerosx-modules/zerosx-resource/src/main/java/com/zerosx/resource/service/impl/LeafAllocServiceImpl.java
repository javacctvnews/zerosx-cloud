package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.resource.dto.LeafAllocDTO;
import com.zerosx.resource.dto.LeafAllocPageDTO;
import com.zerosx.resource.entity.LeafAlloc;
import com.zerosx.resource.mapper.ILeafAllocMapper;
import com.zerosx.resource.service.ILeafAllocService;
import com.zerosx.resource.vo.LeafAllocPageVO;
import com.zerosx.resource.vo.LeafAllocVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 美团分布式ID
 *
 * @author javacctvnews
 * @Description
 * @date 2023-12-05 14:00:46
 */
@Slf4j
@Service
public class LeafAllocServiceImpl extends SuperServiceImpl<ILeafAllocMapper, LeafAlloc> implements ILeafAllocService {

    @Override
    public CustomPageVO<LeafAllocPageVO> pageList(RequestVO<LeafAllocPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), LeafAllocPageVO.class);
    }

    @Override
    public List<LeafAlloc> dataList(LeafAllocPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<LeafAlloc> getWrapper(LeafAllocPageDTO query) {
        LambdaQueryWrapper<LeafAlloc> qw = Wrappers.lambdaQuery(LeafAlloc.class);
        if (query == null) {
            return qw;
        }
        qw.eq(StringUtils.isNotBlank(query.getBizTag()), LeafAlloc::getBizTag, query.getBizTag());
        return qw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LeafAllocDTO leafAllocDTO) {
        LeafAlloc addEntity = BeanCopierUtils.copyProperties(leafAllocDTO, LeafAlloc.class);
        return save(addEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LeafAllocDTO leafAllocDTO) {
        LeafAlloc dbUpdate = getById(leafAllocDTO.getBizTag());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        LeafAlloc updateEntity = BeanCopierUtils.copyProperties(leafAllocDTO, LeafAlloc.class);
        return updateById(updateEntity);
    }

    @Override
    public LeafAllocVO queryById(String id) {
        return EasyTransUtils.copyTrans(getById(id), LeafAllocVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(String[] ids) {
        return removeByIds(Arrays.asList(ids));
    }


    @Override
    public void excelExport(RequestVO<LeafAllocPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), LeafAllocPageVO.class, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String key) {
        baseMapper.updateMaxIdAndGetLeafAlloc(key);
        return getById(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc temp) {
        baseMapper.updateMaxIdByCustomStepAndGetLeafAlloc(temp);
        return getById(temp.getBizTag());
    }

}
