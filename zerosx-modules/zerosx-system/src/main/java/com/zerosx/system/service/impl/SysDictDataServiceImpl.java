package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.api.system.dto.SysDictDataRetrieveDTO;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.common.base.anno.AutoDictData;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.enums.CodeEnum;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.utils.SpringUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.system.dto.SysDictDataDTO;
import com.zerosx.system.dto.SysDictDataUpdateDTO;
import com.zerosx.system.entity.SysDictData;
import com.zerosx.system.entity.SysDictType;
import com.zerosx.system.mapper.ISysDictDataMapper;
import com.zerosx.system.service.ISysDictDataService;
import com.zerosx.system.service.ISysDictTypeService;
import com.zerosx.system.vo.SysDictDataVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author junmy
 * @since 2020-11-18
 */
@Slf4j
@Service
public class SysDictDataServiceImpl extends ServiceImpl<ISysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Autowired
    private ISysDictDataMapper sysDictDataMapper;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Override
    public CustomPageVO<SysDictDataVO> pageList(RequestVO<SysDictDataRetrieveDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), lambdaQW(requestVO.getT())), SysDictDataVO.class);
    }

    @Override
    public boolean insert(SysDictDataDTO sysDictDataDTO) {
        //检查该字典类型下键值是否重复
        LambdaQueryWrapper<SysDictData> countqw = Wrappers.lambdaQuery(SysDictData.class);
        countqw.eq(SysDictData::getDictType, sysDictDataDTO.getDictType());
        countqw.eq(SysDictData::getDictValue, sysDictDataDTO.getDictValue());
        long count = count(countqw);
        if (count > 0) {
            throw new BusinessException("字典类型【" + sysDictDataDTO.getDictType() + "】下已存在键值为【" + sysDictDataDTO.getDictValue() + "】的字典数据");
        }
        SysDictData sysDictData = BeanCopierUtil.copyProperties(sysDictDataDTO, SysDictData.class);
        String userName = ZerosSecurityContextHolder.getUserName();
        sysDictData.setCreateBy(userName);
        sysDictData.setUpdateBy(userName);
        boolean save = save(sysDictData);
        if (save) {
            cacheDictData(sysDictData.getDictType(), null);
        }
        return save;
    }

    @Override
    public List<I18nSelectOptionVO> getSysDictDataSelectList(String dictType) {
        return sysDictDataMapper.getSysDictDataSelectList(dictType);
    }

    @Override
    public Map<String, Map<Object, Object>> getSysDictDataGetMap(List<String> dictType) {
        Map<String, Map<Object, Object>> dictMap = new HashMap<>();
        for (String type : dictType) {
            List<I18nSelectOptionVO> sysDictDataSelectList = sysDictDataMapper.getSysDictDataSelectList(type);
            Map<Object, Object> mapType = new HashMap<>();
            for (I18nSelectOptionVO Option : sysDictDataSelectList) {
                mapType.put(Option.getValue(), Option.getLabel());
            }
            dictMap.put(type, mapType);
        }
        return dictMap;
    }

    @Override
    public List<SysDictData> dataList(SysDictDataRetrieveDTO t) {
        LambdaQueryWrapper<SysDictData> pageqw = lambdaQW(t);
        return list(pageqw);
    }

    private static LambdaQueryWrapper<SysDictData> lambdaQW(SysDictDataRetrieveDTO t) {
        LambdaQueryWrapper<SysDictData> pageqw = Wrappers.lambdaQuery(SysDictData.class);
        pageqw.eq(SysDictData::getDictType, t.getDictType());
        pageqw.like(StringUtils.isNotBlank(t.getDictLabel()), SysDictData::getDictLabel, t.getDictLabel());
        pageqw.like(StringUtils.isNotBlank(t.getStatus()), SysDictData::getStatus, t.getStatus());
        //pageqw.orderByAsc(SysDictData::getDictSort);
        return pageqw;
    }

    @Override
    public List<SysDictDataVO> selectDictDataList(SysDictDataRetrieveDTO sysDictData) {
        return sysDictDataMapper.selectDictDataList(sysDictData);
    }

    @Override
    public boolean updateSysDictData(SysDictDataUpdateDTO sysDictDataUpdateDTO) {
        SysDictData sysDictData = getById(sysDictDataUpdateDTO.getId());
        if (sysDictData == null) {
            return false;
        }
        //先删除缓存
        redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, sysDictData.getDictType()));
        SysDictData updateDictData = BeanCopierUtil.copyProperties(sysDictDataUpdateDTO, SysDictData.class);
        //更新数据库
        return updateById(updateDictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysDictData(Long[] dictCode) {
        for (Long code : dictCode) {
            SysDictData sysDictData = getById(code);
            boolean b = removeById(code);
            if (b) {
                redissonOpService.del(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, sysDictData.getDictType()));
            }
        }
        return true;
    }

    @Override
    public void initCacheDictData(String dictType) {
        SpringUtils.getAopProxy(this).autoSaveDictData();
        LambdaQueryWrapper<SysDictType> qw = Wrappers.lambdaQuery(SysDictType.class);
        qw.eq(StringUtils.isNotBlank(dictType), SysDictType::getDictType, dictType);
        List<SysDictType> list = sysDictTypeService.list(qw);
        list.forEach(e -> cacheDictData(e.getDictType(), null));
    }

    @Override
    public Map<String, String> getDictDataByDictType(String dictType) {
        if (StringUtils.isBlank(dictType)) {
            return new HashMap<>();
        }
        //先查询缓存，缓存没有时查询数据库
        String mapStr = redissonOpService.get(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, dictType));
        if (StringUtils.isNotBlank(mapStr)) {
            return JacksonUtil.toMap(mapStr);
        }
        LambdaQueryWrapper<SysDictData> qow = Wrappers.lambdaQuery(SysDictData.class);
        qow.eq(SysDictData::getDictType, dictType);
        List<SysDictData> list = list(qow);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        //放入缓存
        cacheDictData(dictType, list);
        return list.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
    }

    private void cacheDictData(String dictType, List<SysDictData> list) {
        if (CollectionUtils.isEmpty(list)) {
            LambdaQueryWrapper<SysDictData> qow = Wrappers.lambdaQuery(SysDictData.class);
            qow.eq(SysDictData::getDictType, dictType);
            list = list(qow);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        list.forEach(e -> map.put(e.getDictValue(), e.getDictLabel()));
        redissonOpService.set(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, dictType), JacksonUtil.toJSONString(map));
    }

    @Override
    public SysDictDataVO getDictById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), SysDictDataVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSaveDictData() {
        Reflections reflections = new Reflections(CommonConstants.BASE_PACKAGE);
        Set<Class<? extends CodeEnum>> subTypesOf = reflections.getSubTypesOf(CodeEnum.class);
        for (Class<? extends CodeEnum> clz : subTypesOf) {
            AutoDictData autoDictData = clz.getAnnotation(AutoDictData.class);
            if (autoDictData != null) {
                String dictType = clz.getSimpleName();
                if (StringUtils.isNotBlank(autoDictData.code())) {
                    dictType = autoDictData.code();
                }
                //字典类型
                SysDictType sysDictType = new SysDictType();
                sysDictType.setDictType(dictType);
                sysDictType.setDictName(autoDictData.name());
                LambdaUpdateWrapper<SysDictType> sdtuw = Wrappers.lambdaUpdate(SysDictType.class);
                sdtuw.eq(SysDictType::getDictType, dictType);
                sysDictTypeService.saveOrUpdate(sysDictType, sdtuw);
                //字典类型数据
                CodeEnum[] enumConstants = clz.getEnumConstants();
                List<String> removeValues = new ArrayList<>();
                for (CodeEnum codeEnum : enumConstants) {
                    SysDictData sd = new SysDictData();
                    sd.setDictType(sysDictType.getDictType());
                    sd.setDictLabel(codeEnum.getMessage());
                    sd.setDictValue(String.valueOf(codeEnum.getCode()));
                    LambdaUpdateWrapper<SysDictData> suuw = Wrappers.lambdaUpdate(SysDictData.class);
                    suuw.eq(SysDictData::getDictType, dictType);
                    suuw.eq(SysDictData::getDictValue, sd.getDictValue());
                    saveOrUpdate(sd, suuw);
                    removeValues.add(sd.getDictValue());
                }
                if (CollectionUtils.isNotEmpty(removeValues)) {
                    LambdaQueryWrapper<SysDictData> removeqw = Wrappers.lambdaQuery(SysDictData.class);
                    removeqw.eq(SysDictData::getDictType, dictType);
                    removeqw.notIn(SysDictData::getDictValue, removeValues);
                    remove(removeqw);
                }
            }
        }
    }

}
