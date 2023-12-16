package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RegionSelectVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.resource.dto.AreaCitySourceDTO;
import com.zerosx.resource.dto.AreaCitySourcePageDTO;
import com.zerosx.resource.entity.AreaCitySource;
import com.zerosx.resource.mapper.IAreaCitySourceMapper;
import com.zerosx.resource.service.IAreaCitySourceService;
import com.zerosx.resource.vo.AreaCitySourcePageVO;
import com.zerosx.resource.vo.AreaCitySourceTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 行政区域数据源
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-12 13:48:43
 */
@Slf4j
@Service
public class AreaCitySourceServiceImpl extends SuperServiceImpl<IAreaCitySourceMapper, AreaCitySource> implements IAreaCitySourceService {

    private static final String ROOT_NODE = "000000";

    private static Map<String, String> regionMap = new ConcurrentHashMap<>();

    private static final String regionKey = ZCache.REGION.key();

    @Autowired
    private IAreaCitySourceMapper areaCitySourceMapper;
    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public CustomPageVO<AreaCitySourcePageVO> pageList(RequestVO<AreaCitySourcePageDTO> requestVO, boolean searchCount) {
        AreaCitySourcePageDTO query = requestVO.getT() == null ? new AreaCitySourcePageDTO() : requestVO.getT();
        LambdaQueryWrapper<AreaCitySource> listqw = getWrapper(query);
        IPage<AreaCitySourcePageVO> page = baseMapper.selectPage(PageUtils.of(requestVO, searchCount), listqw)
                .convert((e) -> EasyTransUtils.copyTrans(e, AreaCitySourcePageVO.class));
        return PageUtils.of(page);
    }

    private static LambdaQueryWrapper<AreaCitySource> getWrapper(AreaCitySourcePageDTO query) {
        LambdaQueryWrapper<AreaCitySource> listqw = Wrappers.lambdaQuery(AreaCitySource.class);
        listqw.like(StringUtils.isNotBlank(query.getAreaName()), AreaCitySource::getAreaName, query.getAreaName());
        return listqw;
    }

    @Override
    public boolean add(AreaCitySourceDTO areaCitySourceDTO) {
        if (!ROOT_NODE.equals(areaCitySourceDTO.getParentAreaCode())) {
            AreaCitySource parentArea = getAreaCitySource(areaCitySourceDTO.getParentAreaCode());
            if (parentArea == null) {
                throw new BusinessException("不存在的父区域，新增失败");
            }
        }
        AreaCitySource areaCitySource = getAreaCitySource(areaCitySourceDTO.getAreaCode());
        if (areaCitySource != null) {
            throw new BusinessException("已存在的行政区域，新增失败");
        }
        AreaCitySource addEntity = BeanCopierUtils.copyProperties(areaCitySourceDTO, AreaCitySource.class);
        addEntity.setExtId(IdGenerator.getRandomStr(12));
        return save(addEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(AreaCitySourceDTO areaCitySourceDTO) {
        AreaCitySource areaCitySource = getAreaCitySource(areaCitySourceDTO.getAreaCode());
        if (areaCitySource == null) {
            throw new BusinessException("已存在的行政区域，新增失败");
        }
        AreaCitySource updateEntity = BeanCopierUtils.copyProperties(areaCitySourceDTO, AreaCitySource.class);
        return updateById(updateEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }

    @Override
    public List<AreaCitySourceTreeVO> lazyTreeData(String parentCode) {
        String parentTitle;
        if (ROOT_NODE.equals(parentCode)) {
            parentTitle = "中华人民共和国";
        } else {
            AreaCitySource parentNode = getAreaCitySource(parentCode);
            parentTitle = parentNode == null ? "" : parentNode.getAreaName();
        }
        List<AreaCitySourceTreeVO> areaCitySourceTreeVOS = areaCitySourceMapper.lazyTreeData(parentCode);
        areaCitySourceTreeVOS.forEach(e -> {
            e.setParentTitle(parentTitle);
        });
        return areaCitySourceTreeVOS;
    }

    /**
     * 按areaCode查询
     *
     * @param areaCode
     * @return
     */
    private AreaCitySource getAreaCitySource(String areaCode) {
        LambdaQueryWrapper<AreaCitySource> acsqw = Wrappers.lambdaQuery(AreaCitySource.class);
        acsqw.eq(AreaCitySource::getAreaCode, areaCode).last("limit 1");
        return getOne(acsqw);
    }

    @Override
    public List<RegionSelectVO> getAllArea() {
        long t1 = System.currentTimeMillis();
        try {
            String cacheRegion = redissonOpService.get(regionKey);
            if (StringUtils.isNotBlank(cacheRegion)) {
                log.debug("省市区行政区域【Redis】查询{}ms", System.currentTimeMillis() - t1);
                List<RegionSelectVO> list = JacksonUtil.toList(cacheRegion, RegionSelectVO.class);
                if (CollectionUtils.isNotEmpty(list)) {
                    return list;
                }
            }
        } catch (Exception e) {
            redissonOpService.del(regionKey);
        }
        //省
        LambdaQueryWrapper<AreaCitySource> rootQw = Wrappers.lambdaQuery(AreaCitySource.class);
        rootQw.eq(AreaCitySource::getParentAreaCode, ROOT_NODE);
        List<AreaCitySource> regionList = list(rootQw);
        for (AreaCitySource region : regionList) {
            //市
            LambdaQueryWrapper<AreaCitySource> cityQw = Wrappers.lambdaQuery(AreaCitySource.class);
            cityQw.eq(AreaCitySource::getParentAreaCode, region.getAreaCode());
            List<AreaCitySource> cityList = list(cityQw);
            for (AreaCitySource area : cityList) {
                //区
                LambdaQueryWrapper<AreaCitySource> areaQw = Wrappers.lambdaQuery(AreaCitySource.class);
                areaQw.eq(AreaCitySource::getParentAreaCode, area.getAreaCode());
                List<AreaCitySource> areaList = list(areaQw);
                area.setChildren(areaList);
            }
            region.setChildren(cityList);
        }
        List<RegionSelectVO> regionSelectVOS = BeanCopierUtils.copyProperties(regionList, RegionSelectVO.class);
        if (CollectionUtils.isNotEmpty(regionSelectVOS)) {
            redissonOpService.set(regionKey, JacksonUtil.toJSONString(regionSelectVOS));
        }
        log.debug("省市区行政区域【DB】查询{}ms", System.currentTimeMillis() - t1);
        return regionSelectVOS;
    }

    @Override
    public String getAreaName(String regionCode) {
        if (StringUtils.isBlank(regionCode)) {
            return StringUtils.EMPTY;
        }
        String cacheName = regionMap.get(regionCode);
        if (StringUtils.isNotBlank(cacheName)) {
            return cacheName;
        }
        String redisCacheName = redissonOpService.hGet(ZCache.REGION_HASH.key(), regionCode);
        if (StringUtils.isNotBlank(redisCacheName)) {
            return redisCacheName;
        }
        LambdaQueryWrapper<AreaCitySource> qw = Wrappers.lambdaQuery(AreaCitySource.class);
        qw.eq(AreaCitySource::getAreaCode, regionCode);
        AreaCitySource region = getOne(qw);
        if (region == null) {
            return StringUtils.EMPTY;
        }
        regionMap.put(regionCode, region.getAreaName());
        redissonOpService.hPut(ZCache.REGION_HASH.key(), regionCode, region.getAreaName());
        return region.getAreaName();
    }

    @Override
    public void excelExport(RequestVO<AreaCitySourcePageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), AreaCitySourcePageVO.class, response);
    }
}
