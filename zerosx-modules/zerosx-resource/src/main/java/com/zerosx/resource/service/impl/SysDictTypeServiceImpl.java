package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.resource.dto.SysDictTypeDTO;
import com.zerosx.resource.dto.SysDictTypeRetrieveDTO;
import com.zerosx.resource.dto.SysDictTypeUpdateDTO;
import com.zerosx.resource.entity.SysDictData;
import com.zerosx.resource.entity.SysDictType;
import com.zerosx.resource.mapper.ISysDictTypeMapper;
import com.zerosx.resource.service.ISysDictDataService;
import com.zerosx.resource.service.ISysDictTypeService;
import com.zerosx.resource.vo.SysDictTypeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 字典类型表 服务实现类
 */
@Service
public class SysDictTypeServiceImpl extends SuperServiceImpl<ISysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Override
    public CustomPageVO<SysDictTypeVO> pageList(RequestVO<SysDictTypeRetrieveDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, true), getWrapper(requestVO.getT())), SysDictTypeVO.class);
    }

    @Override
    public boolean saveDictType(SysDictTypeDTO sysDictTypeDTO) {
        //检查是否存在dictType
        LambdaQueryWrapper<SysDictType> countQw = Wrappers.lambdaQuery(SysDictType.class);
        countQw.eq(SysDictType::getDictType, sysDictTypeDTO.getDictType());
        long count = count(countQw);
        if (count > 0) {
            throw new BusinessException("已存在的字典类型编码:" + sysDictTypeDTO.getDictType());
        }
        SysDictType dictType = BeanCopierUtils.copyProperties(sysDictTypeDTO, SysDictType.class);
        return save(dictType);
    }

    @Override
    public boolean updateSysDictType(SysDictTypeUpdateDTO sysDictType) {
        SysDictType dictType = BeanCopierUtils.copyProperties(sysDictType, SysDictType.class);
        return updateById(dictType);
    }

    @Override
    public List<SysDictType> dataList(SysDictTypeRetrieveDTO queryDto) {
        return list(getWrapper(queryDto));
    }

    private static LambdaQueryWrapper<SysDictType> getWrapper(SysDictTypeRetrieveDTO queryDto) {
        LambdaQueryWrapper<SysDictType> pageqw = Wrappers.lambdaQuery(SysDictType.class);
        pageqw.like(StringUtils.isNotBlank(queryDto.getDictType()), SysDictType::getDictType, queryDto.getDictType());
        pageqw.like(StringUtils.isNotBlank(queryDto.getDictName()), SysDictType::getDictName, queryDto.getDictName());
        pageqw.eq(StringUtils.isNotBlank(queryDto.getDictStatus()), SysDictType::getDictStatus, queryDto.getDictStatus());
        pageqw.orderByDesc(SysDictType::getCreateTime);
        return pageqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictType(Long[] ids) {
        for (Long id : ids) {
            SysDictType sysDictType = getById(id);
            if (sysDictType == null) {
                continue;
            }
            //删除字典类型
            removeById(id);
            //删除字典类型数据
            LambdaQueryWrapper<SysDictData> rmqw = Wrappers.lambdaQuery(SysDictData.class);
            rmqw.eq(SysDictData::getDictType, sysDictType.getDictType());
            sysDictDataService.remove(rmqw);
        }
        return true;
    }

    @Override
    public List<SysDictTypeVO> selectByMap(SysDictTypeRetrieveDTO sysDictType) {
        LambdaQueryWrapper<SysDictType> pageqw = Wrappers.lambdaQuery(SysDictType.class);
        pageqw.like(StringUtils.isNotBlank(sysDictType.getDictType()), SysDictType::getDictType, sysDictType.getDictType());
        pageqw.like(StringUtils.isNotBlank(sysDictType.getDictName()), SysDictType::getDictName, sysDictType.getDictName());
        pageqw.eq(StringUtils.isNotBlank(sysDictType.getDictStatus()), SysDictType::getDictStatus, sysDictType.getDictStatus());
        List<SysDictType> list = list(pageqw);
        return BeanCopierUtils.copyProperties(list, SysDictTypeVO.class);
    }

    @Override
    public SysDictTypeVO getDictTypeById(Long dictId) {
        SysDictType dictType = getById(dictId);
        return BeanCopierUtils.copyProperties(dictType, SysDictTypeVO.class);
    }

    @Override
    public void excelExport(RequestVO<SysDictTypeRetrieveDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), SysDictTypeVO.class, response);
    }
}
