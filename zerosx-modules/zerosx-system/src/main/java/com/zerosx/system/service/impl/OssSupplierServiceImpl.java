package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.oss.core.OSSFactory;
import com.zerosx.common.oss.core.client.IOssClientService;
import com.zerosx.common.oss.core.config.IOssConfig;
import com.zerosx.common.oss.enums.OssTypeEnum;
import com.zerosx.common.oss.properties.DefaultOssProperties;
import com.zerosx.system.dto.OssSupplierDTO;
import com.zerosx.system.dto.OssSupplierPageDTO;
import com.zerosx.system.entity.OssSupplier;
import com.zerosx.system.mapper.IOssSupplierMapper;
import com.zerosx.system.service.IOssSupplierService;
import com.zerosx.system.vo.OssSupplierPageVO;
import com.zerosx.system.vo.OssSupplierVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
    public boolean add(OssSupplierDTO ossSupplierDTO) {
        OssSupplier addEntity = BeanCopierUtil.copyProperties(ossSupplierDTO, OssSupplier.class);
        addEntity.setSupplierName(OssTypeEnum.getOssType(addEntity.getSupplierType()).getMessage());
        return save(addEntity);
    }

    @Override
    public boolean update(OssSupplierDTO ossSupplierDTO) {
        OssSupplier dbUpdate = getById(ossSupplierDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        OssSupplier updateEntity = BeanCopierUtil.copyProperties(ossSupplierDTO, OssSupplier.class);
        updateEntity.setSupplierName(OssTypeEnum.getOssType(updateEntity.getSupplierType()).getMessage());
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
        OSSFactory.refreshClient(OssTypeEnum.getOssType(updateEntity.getSupplierType()), getOssConfig(updateEntity));
        return update;
    }

    @Override
    public OssSupplierVO queryById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), OssSupplierVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    private IOssConfig getOssConfig(OssSupplier ossSupplier) {
        OssTypeEnum ossTypeEnum = OssTypeEnum.getOssType(ossSupplier.getSupplierType());
        Class<? extends IOssConfig> configClz = ossTypeEnum.getConfigClz();
        IOssConfig ossConfig = BeanCopierUtil.copyProperties(ossSupplier, configClz);
        ossConfig.setOssSupplierId(ossSupplier.getId());
        /*if (OssTypeEnum.ALIBABA.getCode().equals(ossSupplier.getSupplierType())) {
            AliyunOssConfig aliyunOssConfig = new AliyunOssConfig();
            aliyunOssConfig.setOssSupplierId(ossSupplier.getId());
            aliyunOssConfig.setSupplierType(OssTypeEnum.ALIBABA.getCode());
            aliyunOssConfig.setEndpoint(ossSupplier.getEndpoint());
            aliyunOssConfig.setRegionId(ossSupplier.getRegionId());
            aliyunOssConfig.setAccessKeyId(ossSupplier.getAccessKeyId());
            aliyunOssConfig.setAccessKeySecret(ossSupplier.getAccessKeySecret());
            aliyunOssConfig.setBucketName(ossSupplier.getBucketName());
            return aliyunOssConfig;
        } else if (OssTypeEnum.QINIU.getCode().equals(ossSupplier.getSupplierType())) {
            QiniuOssConfig qiniuOssConfig = new QiniuOssConfig();
            qiniuOssConfig.setOssSupplierId(ossSupplier.getId());
            qiniuOssConfig.setSupplierType(OssTypeEnum.QINIU.getCode());
            qiniuOssConfig.setDomainAddress(ossSupplier.getDomainAddress());
            qiniuOssConfig.setRegionId(ossSupplier.getRegionId());
            qiniuOssConfig.setAccessKeyId(ossSupplier.getAccessKeyId());
            qiniuOssConfig.setAccessKeySecret(ossSupplier.getAccessKeySecret());
            qiniuOssConfig.setBucketName(ossSupplier.getBucketName());
            return qiniuOssConfig;
        } else if (OssTypeEnum.TENCENT.getCode().equals(ossSupplier.getSupplierType())) {
            TencentOssConfig tencentOssConfig = new TencentOssConfig();
            tencentOssConfig.setOssSupplierId(ossSupplier.getId());
            tencentOssConfig.setSupplierType(OssTypeEnum.TENCENT.getCode());
            tencentOssConfig.setRegionId(ossSupplier.getRegionId());
            tencentOssConfig.setAccessKeyId(ossSupplier.getAccessKeyId());
            tencentOssConfig.setAccessKeySecret(ossSupplier.getAccessKeySecret());
            tencentOssConfig.setBucketName(ossSupplier.getBucketName());
            return tencentOssConfig;
        }*/
        return ossConfig;
    }

    @Override
    public IOssClientService getClient(Long ossSupplierId) {
        OssSupplier ossSupplier;
        //查询配置
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
                IOssConfig obj = BeanCopierUtil.copyProperties(defaultOssProperties.getOssConfig(), clz);
                return OSSFactory.createClient(OssTypeEnum.getOssType(ossType), obj);
            } else {
                OssSupplierPageDTO pageDTO = new OssSupplierPageDTO();
                pageDTO.setOperatorId(operatorId);
                pageDTO.setStatus(StatusEnum.NORMAL.getCode());
                List<OssSupplier> ossSuppliers = dataList(pageDTO);
                if (CollectionUtils.isEmpty(ossSuppliers)) {
                    throw new BusinessException("未配置或无可用的OSS服务商");
                }
                ossSupplier = ossSuppliers.get(0);
            }
        } else {
            ossSupplier = getById(ossSupplierId);
        }
        IOssConfig ossConfig = getOssConfig(ossSupplier);
        return OSSFactory.createClient(OssTypeEnum.getOssType(ossSupplier.getSupplierType()), ossConfig);
    }

}
