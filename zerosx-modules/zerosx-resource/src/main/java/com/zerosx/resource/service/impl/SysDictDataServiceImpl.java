package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.system.dto.SysDictDataPageDTO;
import com.zerosx.common.anno.AutoDictData;
import com.zerosx.common.base.BaseEnum;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.constants.ZCacheKey;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.anno.ClearCache;
import com.zerosx.common.core.enums.CssTypeEnum;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.common.utils.SpringUtils;
import com.zerosx.resource.dto.SysDictDataDTO;
import com.zerosx.resource.dto.SysDictDataUpdateDTO;
import com.zerosx.resource.entity.SysDictData;
import com.zerosx.resource.entity.SysDictType;
import com.zerosx.resource.mapper.ISysDictDataMapper;
import com.zerosx.resource.service.ISysDictDataService;
import com.zerosx.resource.service.ISysDictTypeService;
import com.zerosx.resource.task.ResourceAsyncTask;
import com.zerosx.resource.vo.SysDictDataVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class SysDictDataServiceImpl extends SuperServiceImpl<ISysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Autowired
    private ResourceAsyncTask resourceAsyncTask;

    @Override
    public CustomPageVO<SysDictDataVO> pageList(RequestVO<SysDictDataPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SysDictDataVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(SysDictDataDTO sysDictDataDTO) {
        SysDictData sysDictData = BeanCopierUtils.copyProperties(sysDictDataDTO, SysDictData.class);
        //检查该字典类型下键值是否重复
        checkExist(sysDictData, "字典类型【%s】下已存在键值为【%s】的字典数据", sysDictData.getDictType(), sysDictData.getDictValue());
        return save(sysDictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ClearCache(keys = ZCacheKey.DICT_DATA, field = "#sysDictDataUpdateDTO.dictType")
    public boolean update(SysDictDataUpdateDTO sysDictDataUpdateDTO) {
        SysDictData sysDictData = getById(sysDictDataUpdateDTO.getId());
        if (sysDictData == null) {
            return false;
        }
        SysDictData updateDictData = BeanCopierUtils.copyProperties(sysDictDataUpdateDTO, SysDictData.class);
        updateDictData.setDictType(sysDictData.getDictType());
        //是否已经存在
        checkExist(updateDictData, "字典类型【%s】下已存在键值为【%s】的字典数据", sysDictData.getDictType(), sysDictDataUpdateDTO.getDictValue());
        //更新数据库
        return updateById(updateDictData);
    }

    @Override
    protected void checkExist(SysDictData sysDictData, String message, Object... args) {
        LambdaQueryWrapper<SysDictData> editqw = Wrappers.lambdaQuery(SysDictData.class);
        editqw.ne(sysDictData.getId() != null, SysDictData::getId, sysDictData.getId());
        editqw.eq(SysDictData::getDictType, sysDictData.getDictType());
        editqw.eq(SysDictData::getDictValue, sysDictData.getDictValue());
        super.checkExist(editqw, message, args);
    }

    @Override
    public List<I18nSelectOptionVO> getDictList(String dictType) {
        return getCacheSysDictData(dictType);
    }

    @Override
    public List<SysDictData> dataList(SysDictDataPageDTO sysDictDataPageDTO) {
        LambdaQueryWrapper<SysDictData> pageqw = getWrapper(sysDictDataPageDTO);
        return list(pageqw);
    }

    private LambdaQueryWrapper<SysDictData> getWrapper(SysDictDataPageDTO t) {
        LambdaQueryWrapper<SysDictData> pageqw = Wrappers.lambdaQuery(SysDictData.class);
        pageqw.eq(SysDictData::getDictType, t.getDictType());
        pageqw.like(StringUtils.isNotBlank(t.getDictLabel()), SysDictData::getDictLabel, t.getDictLabel());
        pageqw.like(StringUtils.isNotBlank(t.getStatus()), SysDictData::getStatus, t.getStatus());
        return pageqw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysDictData(Long[] dictCode) {
        for (Long code : dictCode) {
            SysDictData sysDictData = getById(code);
            boolean b = removeById(code);
            if (b) {
                redissonOpService.del(ZCache.DICT_DATA.key(sysDictData.getDictType()));
            }
        }
        return true;
    }

    @Override
    public void initCacheDictData(String dictType) {
        SpringUtils.getAopProxy(this).loadSysDictData("");
    }

    @Override
    public Map<Object, String> getDictDataMap(String dictType) {
        List<I18nSelectOptionVO> cacheSysDictData = getCacheSysDictData(dictType);
        return cacheSysDictData.stream().collect(Collectors.toMap(I18nSelectOptionVO::getValue, I18nSelectOptionVO::getLabel));
    }

    @Override
    public SysDictDataVO getDictById(Long id) {
        return BeanCopierUtils.copyProperties(getById(id), SysDictDataVO.class);
    }

    @Override
    public void excelExport(RequestVO<SysDictDataPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), SysDictDataVO.class, response);
    }

    /**
     * 扫描加载贴有@AutoDictData的枚举数据字典
     *
     * @param queryDictType
     * @return 相同queryDictType时有返回，否则为空
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysDictData> loadSysDictData(String queryDictType) {
        List<SysDictData> resList = new ArrayList<>();
        Reflections reflections = new Reflections(CommonConstants.BASE_PACKAGE);
        Set<Class<? extends BaseEnum>> subTypesOf = reflections.getSubTypesOf(BaseEnum.class);
        for (Class<? extends BaseEnum> clz : subTypesOf) {
            AutoDictData autoDictData = clz.getAnnotation(AutoDictData.class);
            if (autoDictData != null) {
                String dictType = clz.getSimpleName();
                if (StringUtils.isNotBlank(autoDictData.code())) {
                    dictType = autoDictData.code();
                }
                //匹配加载
                if (StringUtils.isNotBlank(queryDictType)) {
                    //匹配到则返回
                    if (dictType.equals(queryDictType)) {
                        return getSysDictData(clz, dictType, autoDictData);
                    }
                } else {
                    //扫描加载全部
                    getSysDictData(clz, dictType, autoDictData);
                }
            }
        }
        return resList;
    }

    private List<SysDictData> getSysDictData(Class<? extends BaseEnum> clz, String dictType, AutoDictData autoDictData) {
        //先删除缓存
        redissonOpService.del(ZCache.DICT_DATA.key(dictType));
        //字典类型
        SysDictType sysDictType = new SysDictType();
        sysDictType.setDictType(dictType);
        sysDictType.setDictName(autoDictData.name());
        sysDictType.setRemarks(StringUtils.isBlank(autoDictData.desc()) ? autoDictData.name() : autoDictData.desc());
        LambdaUpdateWrapper<SysDictType> sdtuw = Wrappers.lambdaUpdate(SysDictType.class);
        sdtuw.eq(SysDictType::getDictType, dictType);
        sysDictTypeService.saveOrUpdate(sysDictType, sdtuw);
        //字典类型数据
        BaseEnum[] enumConstants = clz.getEnumConstants();
        for (BaseEnum baseEnum : enumConstants) {
            SysDictData sd = new SysDictData();
            sd.setDictType(sysDictType.getDictType());
            sd.setDictLabel(baseEnum.getMessage());
            sd.setDictValue(String.valueOf(baseEnum.getCode()));
            sd.setListClass(StringUtils.isBlank(baseEnum.getCss()) ? CssTypeEnum.DEFAULT.getCss() : baseEnum.getCss());
            LambdaUpdateWrapper<SysDictData> suuw = Wrappers.lambdaUpdate(SysDictData.class);
            suuw.eq(SysDictData::getDictType, dictType);
            suuw.eq(SysDictData::getDictValue, sd.getDictValue());
            saveOrUpdate(sd, suuw);
        }
        LambdaQueryWrapper<SysDictData> dqw = Wrappers.lambdaQuery(SysDictData.class);
        dqw.eq(SysDictData::getDictType, dictType);
        List<SysDictData> resList = list(dqw);
        //再次删除
        resourceAsyncTask.asyncRedisDelOptions(ZCache.DICT_DATA.key(dictType));
        return resList;
    }

    private List<I18nSelectOptionVO> getCacheSysDictData(String dictType) {
        if (StringUtils.isBlank(dictType)) {
            return new ArrayList<>();
        }
        //先查询缓存，缓存没有时查询数据库
        String mapStr = redissonOpService.get(ZCache.DICT_DATA.key(dictType));
        if (StringUtils.isNotBlank(mapStr)) {
            return JacksonUtil.toList(mapStr, I18nSelectOptionVO.class);
        }
        LambdaQueryWrapper<SysDictData> qow = Wrappers.lambdaQuery(SysDictData.class);
        qow.eq(SysDictData::getDictType, dictType);
        List<SysDictData> dictData = list(qow);
        //放入缓存
        if (CollectionUtils.isEmpty(dictData)) {
            dictData = SpringUtils.getAopProxy(this).loadSysDictData(dictType);
        }
        if (CollectionUtils.isNotEmpty(dictData)) {
            List<I18nSelectOptionVO> vos = transferDictData(dictData);
            redissonOpService.set(ZCache.DICT_DATA.key(dictType), JacksonUtil.toJSONString(vos));
            return vos;
        }
        return new ArrayList<>();
    }

    private static List<I18nSelectOptionVO> transferDictData(List<SysDictData> vos) {
        List<I18nSelectOptionVO> resOpt = new ArrayList<>();
        if (CollectionUtils.isEmpty(vos)) {
            return resOpt;
        }
        I18nSelectOptionVO vo;
        for (SysDictData dictData : vos) {
            vo = new I18nSelectOptionVO();
            vo.setValue(dictData.getDictValue());
            vo.setLabel(dictData.getDictLabel());
            vo.setCssClass(dictData.getCssClass());
            vo.setListClass(dictData.getListClass());
            vo.setI18nCode(dictData.getDictType() + "." + dictData.getDictValue());
            resOpt.add(vo);
        }
        return resOpt;
    }

}
