package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.resource.dto.SysParamDTO;
import com.zerosx.resource.dto.SysParamPageDTO;
import com.zerosx.resource.entity.SysParam;
import com.zerosx.resource.mapper.ISysParamMapper;
import com.zerosx.resource.service.ISysParamService;
import com.zerosx.resource.vo.SysParamPageVO;
import com.zerosx.resource.vo.SysParamVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统参数
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-29 01:02:29
 */
@Slf4j
@Service
public class SysParamServiceImpl extends SuperServiceImpl<ISysParamMapper, SysParam> implements ISysParamService {

    @Override
    public CustomPageVO<SysParamPageVO> pageList(RequestVO<SysParamPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SysParamPageVO.class);
    }

    @Override
    public List<SysParam> dataList(SysParamPageDTO query) {
        LambdaQueryWrapper<SysParam> listqw = getWrapper(query);
        return list(listqw);
    }

    private static LambdaQueryWrapper<SysParam> getWrapper(SysParamPageDTO query) {
        LambdaQueryWrapper<SysParam> listqw = Wrappers.lambdaQuery(SysParam.class);
        if (query == null) {
            return listqw;
        }
        listqw.eq(StringUtils.isNotBlank(query.getStatus()), SysParam::getStatus, query.getStatus());
        listqw.like(StringUtils.isNotBlank(query.getParamName()), SysParam::getParamName, query.getParamName());
        listqw.eq(StringUtils.isNotBlank(query.getOperatorId()), SysParam::getOperatorId, query.getOperatorId());
        listqw.eq(StringUtils.isNotBlank(query.getParamScope()), SysParam::getParamScope, query.getParamScope());
        listqw.orderByDesc(SysParam::getCreateTime);
        return listqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SysParamDTO sysParamDTO) {
        SysParam addEntity = BeanCopierUtils.copyProperties(sysParamDTO, SysParam.class);
        //校验重复
        if ("0".equals(sysParamDTO.getParamScope())) {
            LambdaQueryWrapper<SysParam> listqw = Wrappers.lambdaQuery(SysParam.class);
            listqw.eq(SysParam::getParamKey, sysParamDTO.getParamKey());
            if (count(listqw) > 0) {
                throw new BusinessException(String.format("已存在参数编码是【%s】的系统参数", sysParamDTO.getParamKey()));
            }
            return save(addEntity);
        } else if ("1".equals(sysParamDTO.getParamScope())) {
            LambdaQueryWrapper<SysParam> listqw = Wrappers.lambdaQuery(SysParam.class);
            listqw.eq(SysParam::getParamKey, sysParamDTO.getParamKey());
            listqw.eq(SysParam::getOperatorId, sysParamDTO.getOperatorId());
            if (count(listqw) > 0) {
                throw new BusinessException(String.format("租户已存在参数编码是【%s】的系统参数", sysParamDTO.getParamKey()));
            }
            return save(addEntity);
        }
        throw new BusinessException(ResultEnum.PARAM_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysParamDTO sysParamDTO) {
        SysParam dbUpdate = getById(sysParamDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SysParam updateEntity = BeanCopierUtils.copyProperties(sysParamDTO, SysParam.class);
        //校验重复
        if ("0".equals(sysParamDTO.getParamScope())) {
            LambdaQueryWrapper<SysParam> listqw = Wrappers.lambdaQuery(SysParam.class);
            listqw.eq(SysParam::getParamKey, sysParamDTO.getParamKey());
            listqw.ne(SysParam::getId, sysParamDTO.getId());
            if (count(listqw) > 0) {
                throw new BusinessException(String.format("已存在参数编码是【%s】的系统参数", sysParamDTO.getParamKey()));
            }
            return updateById(updateEntity);
        } else if ("1".equals(sysParamDTO.getParamScope())) {
            LambdaQueryWrapper<SysParam> listqw = Wrappers.lambdaQuery(SysParam.class);
            listqw.eq(SysParam::getParamKey, sysParamDTO.getParamKey());
            listqw.eq(SysParam::getOperatorId, sysParamDTO.getOperatorId());
            listqw.ne(SysParam::getId, sysParamDTO.getId());
            if (count(listqw) > 0) {
                throw new BusinessException(String.format("租户已存在参数编码是【%s】的系统参数", sysParamDTO.getParamKey()));
            }
            return updateById(updateEntity);
        }
        throw new BusinessException(ResultEnum.PARAM_ERROR);
    }

    @Override
    public SysParamVO queryById(Long id) {
        return EasyTransUtils.copyTrans(getById(id), SysParamVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        for (Long id : ids) {
            baseMapper.deleteById(id);
        }
        return true;
    }

    @Override
    public SysParamVO queryByKey(SysParamPageDTO sysParamPageDTO) {
        LambdaQueryWrapper<SysParam> qw = Wrappers.lambdaQuery(SysParam.class);
        qw.eq(SysParam::getParamKey, sysParamPageDTO.getParamKey());
        qw.eq(SysParam::getParamScope, sysParamPageDTO.getParamScope());
        qw.eq(SysParam::getStatus, "0");
        if ("1".equals(sysParamPageDTO.getParamScope())) {
            if (StringUtils.isBlank(sysParamPageDTO.getOperatorId())) {
                throw new BusinessException("租户标识不能为空");
            }
            qw.eq(SysParam::getOperatorId, sysParamPageDTO.getOperatorId());
        }
        SysParam sysParam = getOne(qw);
        return BeanCopierUtils.copyProperties(sysParam, SysParamVO.class);
    }

    @Override
    public void excelExport(RequestVO<SysParamPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), SysParamPageVO.class, response);
    }

}
