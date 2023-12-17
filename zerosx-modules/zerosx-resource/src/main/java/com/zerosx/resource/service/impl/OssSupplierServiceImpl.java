package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.core.OSSFactory;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.enums.OssTypeEnum;
import com.zerosx.common.oss.properties.DefaultOssProperties;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.resource.dto.OssSupplierDTO;
import com.zerosx.resource.dto.OssSupplierPageDTO;
import com.zerosx.resource.entity.OssSupplier;
import com.zerosx.resource.mapper.IOssSupplierMapper;
import com.zerosx.resource.service.IOssSupplierService;
import com.zerosx.resource.vo.OssSupplierPageVO;
import com.zerosx.resource.vo.OssSupplierVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OSS配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-08 18:23:18
 */
@Slf4j
@Service
public class OssSupplierServiceImpl extends SuperServiceImpl<IOssSupplierMapper, OssSupplier> implements IOssSupplierService {

    @Autowired
    private DefaultOssProperties defaultOssProperties;

    @Override
    public CustomPageVO<OssSupplierPageVO> pageList(RequestVO<OssSupplierPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), OssSupplierPageVO.class);
    }

    @Override
    public List<OssSupplier> dataList(OssSupplierPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<OssSupplier> getWrapper(OssSupplierPageDTO query) {
        LambdaQueryWrapper<OssSupplier> qw = Wrappers.lambdaQuery(OssSupplier.class);
        if (query == null) {
            return qw;
        }
        qw.eq(StringUtils.isNotBlank(query.getOperatorId()), OssSupplier::getOperatorId, query.getOperatorId());
        qw.eq(StringUtils.isNotBlank(query.getStatus()), OssSupplier::getStatus, query.getStatus());
        qw.eq(StringUtils.isNotBlank(query.getSupplierType()), OssSupplier::getSupplierType, query.getSupplierType());
        return qw;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(OssSupplierDTO ossSupplierDTO) {
        OssSupplier addEntity = BeanCopierUtils.copyProperties(ossSupplierDTO, OssSupplier.class);
        addEntity.setSupplierName(OssTypeEnum.getOssType(addEntity.getSupplierType()).getMessage());
        checkExist(addEntity, "已存在的存储桶名称：%s，禁止重复添加", ossSupplierDTO.getBucketName());
        return save(addEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(OssSupplierDTO ossSupplierDTO) {
        OssSupplier dbUpdate = getById(ossSupplierDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        OssSupplier updateEntity = BeanCopierUtils.copyProperties(ossSupplierDTO, OssSupplier.class);
        updateEntity.setSupplierName(OssTypeEnum.getOssType(updateEntity.getSupplierType()).getMessage());
        checkExist(updateEntity, "已存在的存储桶名称：%s", ossSupplierDTO.getBucketName());
        boolean update = updateById(updateEntity);
        if (update && StatusEnum.NORMAL.getCode().equals(updateEntity.getStatus())) {
            //更新其他oss厂商停用
            LambdaUpdateWrapper<OssSupplier> uw = Wrappers.lambdaUpdate(OssSupplier.class);
            uw.set(OssSupplier::getStatus, StatusEnum.ABNORMAL.getCode());
            uw.eq(OssSupplier::getOperatorId, ossSupplierDTO.getOperatorId());
            uw.ne(OssSupplier::getId, ossSupplierDTO.getId());
            update(uw);
        }
        //刷新实例
        OSSFactory.refreshClient(OssTypeEnum.getOssType(updateEntity.getSupplierType()), buildOssConfig(updateEntity));
        return update;
    }

    @Override
    protected void checkExist(OssSupplier ossSupplier, String message, Object... args) {
        LambdaQueryWrapper<OssSupplier> qw = Wrappers.lambdaQuery(OssSupplier.class);
        qw.ne(ossSupplier.getId() != null, OssSupplier::getId, ossSupplier.getId());
        qw.eq(OssSupplier::getOperatorId, ossSupplier.getOperatorId());
        qw.eq(OssSupplier::getSupplierType, ossSupplier.getSupplierType());
        qw.eq(OssSupplier::getBucketName, ossSupplier.getBucketName());
        super.checkExist(qw, message, args);
    }

    @Override
    public OssSupplierVO queryById(Long id) {
        return EasyTransUtils.copyTrans(getById(id), OssSupplierVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        for (Long id : ids) {
            baseMapper.deleteById(id);
        }
        return true;
    }

    private IOssConfig buildOssConfig(OssSupplier ossSupplier) {
        if (ossSupplier == null) {
            throw new BusinessException("OSS配置不能为空");
        }
        OssTypeEnum ossTypeEnum = OssTypeEnum.getOssType(ossSupplier.getSupplierType());
        Class<? extends IOssConfig> configClz = ossTypeEnum.getConfigClz();
        IOssConfig ossConfig = BeanCopierUtils.copyProperties(ossSupplier, configClz);
        ossConfig.setOssSupplierId(ossSupplier.getId());
        return ossConfig;
    }

    @Override
    public IOssClientService getClient(Long ossSupplierId) {
        OssSupplier ossSupplier;
        //查询配置
        OssSupplierPageDTO pageDTO = new OssSupplierPageDTO();
        if (ossSupplierId == null) {
            String operatorId = ZerosSecurityContextHolder.getOperatorIds();
            if (StringUtils.isBlank(operatorId)) {
                String ossType = defaultOssProperties.getOssConfig().getSupplierType();
                String ossConfigClz = defaultOssProperties.getOssConfigClz();
                Class<? extends IOssConfig> clz;
                try {
                    clz = (Class<? extends IOssConfig>) Class.forName(ossConfigClz);
                } catch (ClassNotFoundException e) {
                    throw new BusinessException("配置类没有找到:" + ossConfigClz);
                }
                IOssConfig obj = BeanCopierUtils.copyProperties(defaultOssProperties.getOssConfig(), clz);
                return OSSFactory.createClient(OssTypeEnum.getOssType(ossType), obj);
            } else {
                pageDTO.setOperatorId(operatorId);
                pageDTO.setStatus(StatusEnum.NORMAL.getCode());
            }
        } else {
            pageDTO.setId(ossSupplierId);
            pageDTO.setStatus(StatusEnum.NORMAL.getCode());
        }
        ossSupplier = baseMapper.selectOssSupplier(pageDTO);
        IOssConfig ossConfig = buildOssConfig(ossSupplier);
        return OSSFactory.createClient(OssTypeEnum.getOssType(ossSupplier.getSupplierType()), ossConfig);
    }

    @Override
    public void excelExport(RequestVO<OssSupplierPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), OssSupplierPageVO.class, response);
    }
}
