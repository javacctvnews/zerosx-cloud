package com.zerosx.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.resource.dto.SmsSendDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.resource.dto.SmsSupplierDTO;
import com.zerosx.resource.dto.SmsSupplierPageDTO;
import com.zerosx.resource.entity.SmsSupplier;
import com.zerosx.resource.entity.SmsSupplierBusiness;
import com.zerosx.resource.mapper.ISmsSupplierMapper;
import com.zerosx.resource.service.ISmsSupplierBusinessService;
import com.zerosx.resource.service.ISmsSupplierService;
import com.zerosx.resource.vo.SmsSupplierPageVO;
import com.zerosx.resource.vo.SmsSupplierVO;
import com.zerosx.sms.core.SmsFactory;
import com.zerosx.sms.core.config.ISupplierConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import com.zerosx.sms.model.SmsRequest;
import com.zerosx.sms.model.SmsResponse;
import com.zerosx.sms.properties.CustomSmsProperties;
import com.zerosx.sms.properties.SmsBusinessProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 短信配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-30 18:28:13
 */
@Slf4j
@Service
public class SmsSupplierServiceImpl extends SuperServiceImpl<ISmsSupplierMapper, SmsSupplier> implements ISmsSupplierService {

    @Autowired
    private CustomSmsProperties customSmsProperties;
    @Autowired
    private ISmsSupplierBusinessService smsSupplierBusinessService;

    @Override
    public CustomPageVO<SmsSupplierPageVO> pageList(RequestVO<SmsSupplierPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SmsSupplierPageVO.class);
    }

    @Override
    public List<SmsSupplier> dataList(SmsSupplierPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<SmsSupplier> getWrapper(SmsSupplierPageDTO query) {
        LambdaQueryWrapper<SmsSupplier> qw = Wrappers.lambdaQuery(SmsSupplier.class);
        if (query == null) {
            return qw;
        }
        //todo
        return qw;
    }

    @Override
    public boolean add(SmsSupplierDTO smsSupplierDTO) {
        checkSmsSupplier(smsSupplierDTO);
        SmsSupplier addEntity = BeanCopierUtils.copyProperties(smsSupplierDTO, SmsSupplier.class);
        addEntity.setSupplierName(SupplierTypeEnum.getSupplierName(smsSupplierDTO.getSupplierType()));
        return save(addEntity);
    }

    @Override
    public boolean update(SmsSupplierDTO smsSupplierDTO) {
        SmsSupplier dbUpdate = getById(smsSupplierDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        if (!dbUpdate.getSupplierType().equals(smsSupplierDTO.getSupplierType())) {
            checkSmsSupplier(smsSupplierDTO);
        }
        SmsSupplier updateEntity = BeanCopierUtils.copyProperties(smsSupplierDTO, SmsSupplier.class);
        updateEntity.setSupplierName(SupplierTypeEnum.getSupplierName(smsSupplierDTO.getSupplierType()));
        refreshClient(updateEntity);
        boolean update = updateById(updateEntity);
        if (update && StatusEnum.NORMAL.getCode().equals(updateEntity.getStatus())) {
            //更新其他sms厂商停用
            LambdaUpdateWrapper<SmsSupplier> uw = Wrappers.lambdaUpdate(SmsSupplier.class);
            uw.set(SmsSupplier::getStatus, StatusEnum.ABNORMAL.getCode());
            uw.eq(SmsSupplier::getOperatorId, smsSupplierDTO.getOperatorId());
            uw.ne(SmsSupplier::getId, smsSupplierDTO.getId());
            update(uw);
        }
        return update;
    }

    private void checkSmsSupplier(SmsSupplierDTO smsSupplierDTO) {
        LambdaQueryWrapper<SmsSupplier> qw = Wrappers.lambdaQuery(SmsSupplier.class);
        qw.eq(SmsSupplier::getOperatorId, smsSupplierDTO.getOperatorId());
        qw.eq(SmsSupplier::getSupplierType, smsSupplierDTO.getSupplierType());
        long count = count(qw);
        if (count > 0) {
            throw new BusinessException("已存在相同的短信服务商：" + SupplierTypeEnum.getSupplierName(smsSupplierDTO.getSupplierType()));
        }
    }

    /**
     * 刷新实例
     *
     * @param smsSupplier
     */
    public void refreshClient(SmsSupplier smsSupplier) {
        ISupplierConfig smsConfig = getSmsConfig(smsSupplier);
        if (smsConfig == null) {
            return;
        }
        if (SupplierTypeEnum.ALIBABA.getCode().equals(smsSupplier.getSupplierType())) {
            SmsFactory.refreshClient(SupplierTypeEnum.ALIBABA, smsConfig);
        } else if (SupplierTypeEnum.JUHE.getCode().equals(smsSupplier.getSupplierType())) {
            SmsFactory.refreshClient(SupplierTypeEnum.JUHE, smsConfig);
        }
    }

    @Override
    public SmsSupplierVO queryById(Long id) {
        return EasyTransUtils.copyTrans(getById(id), SmsSupplierVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        for (Long id : ids) {
            //删除短信业务
            LambdaQueryWrapper<SmsSupplierBusiness> wrapper = Wrappers.lambdaQuery(SmsSupplierBusiness.class);
            wrapper.eq(SmsSupplierBusiness::getSmsSupplierId, id);
            smsSupplierBusinessService.remove(wrapper);
            SmsSupplier supplier = getById(id);
            if (supplier != null) {
                //移除配置
                SmsFactory.removeClient(SupplierTypeEnum.getSupplierType(supplier.getSupplierType()), supplier.getOperatorId());
            }
        }
        //删除短信配置
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public ResultVO<?> sendSms(SmsSendDTO smsSendDTO) {
        ISupplierConfig smsConfig;
        SmsRequest smsRequest;
        String supplierType;
        if (StringUtils.isBlank(smsSendDTO.getOperatorId())) {
            Class<? extends ISupplierConfig> clz;
            try {
                clz = (Class<? extends ISupplierConfig>) Class.forName(customSmsProperties.getSmsConfigClass());
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException("短信配置错误，请检查配置");
            }
            smsConfig = BeanCopierUtils.copyProperties(customSmsProperties.getDefaultSms(), clz);
            supplierType = smsConfig.getSupplierType();
            List<SmsBusinessProperties> smsBusinessProperties = customSmsProperties.getSmsBusinesses();
            if (CollectionUtils.isEmpty(smsBusinessProperties)) {
                throw new BusinessException("短信配置的业务模板未配置，请检查配置");
            }
            Map<String, SmsBusinessProperties> businessPropertiesMap = smsBusinessProperties.stream().collect(Collectors.toMap(SmsBusinessProperties::getBusinessCode, Function.identity()));
            SmsBusinessProperties smsBusiness = businessPropertiesMap.get(smsSendDTO.getBusinessCode());
            if (smsBusiness == null) {
                throw new BusinessException("短信配置的业务模板" + smsSendDTO.getBusinessCode() + "未配置，请检查配置");
            }
            String signature = StringUtils.isBlank(smsBusiness.getSignature()) ? customSmsProperties.getDefaultSms().getSignature() : smsBusiness.getSignature();
            smsRequest = getSmsRequest(smsSendDTO, signature, smsBusiness.getTemplateCode());
        } else {
            //运营商可用的短信配置
            LambdaQueryWrapper<SmsSupplier> qwSs = Wrappers.lambdaQuery(SmsSupplier.class);
            qwSs.eq(SmsSupplier::getOperatorId, smsSendDTO.getOperatorId());
            qwSs.eq(SmsSupplier::getStatus, StatusEnum.NORMAL.getCode());
            SmsSupplier supplier = getOne(qwSs);
            if (supplier == null) {
                throw new BusinessException("短信配置为空，请检查配置");
            }
            if (!StatusEnum.NORMAL.getCode().equals(supplier.getStatus())) {
                throw new BusinessException("短信配置已停用，请检查配置");
            }
            supplierType = supplier.getSupplierType();
            smsConfig = getSmsConfig(supplier);
            //查询业务记录
            LambdaQueryWrapper<SmsSupplierBusiness> qwSsb = Wrappers.lambdaQuery(SmsSupplierBusiness.class);
            qwSsb.eq(SmsSupplierBusiness::getOperatorId, smsSendDTO.getOperatorId());
            qwSsb.eq(SmsSupplierBusiness::getBusinessCode, smsSendDTO.getBusinessCode());
            qwSsb.eq(SmsSupplierBusiness::getSmsSupplierId, supplier.getId());
            SmsSupplierBusiness ssb = smsSupplierBusinessService.getOne(qwSsb);
            if (ssb == null) {
                throw new BusinessException("没有可用的【" + smsSendDTO.getBusinessCode() + "】的短信业务模板，请检查配置");
            }
            if (!StatusEnum.NORMAL.getCode().equals(ssb.getStatus())) {
                throw new BusinessException("【" + smsSendDTO.getBusinessCode() + "】的短信业务模板已停用，请检查配置");
            }
            String signature = StringUtils.isBlank(ssb.getSignature()) ? supplier.getSignature() : ssb.getSignature();
            smsRequest = getSmsRequest(smsSendDTO, signature, ssb.getTemplateCode());
        }
        if (smsConfig == null) {
            throw new BusinessException("【ISupplierConfig】配置为空");
        }
        //发送短信
        SmsResponse smsResponse = SmsFactory.createSmsClient(SupplierTypeEnum.getSupplierType(supplierType), smsConfig).sendMessage(smsRequest);
        if (smsResponse.getSendCode() == 0) {
            return ResultVOUtil.success(smsResponse);
        }
        return ResultVOUtil.error(smsResponse.getSendCode(), smsResponse.getSendMsg());
    }

    private SmsRequest getSmsRequest(SmsSendDTO smsSendDTO, String signature, String templateCode) {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setSignature(signature);
        smsRequest.setTemplateParam(JacksonUtil.toJSONString(smsSendDTO.getParams()));
        smsRequest.setTemplateCode(templateCode);
        smsRequest.setPhoneNumbers(smsSendDTO.getPhoneNumbers());
        smsRequest.setOperatorId(smsSendDTO.getOperatorId());
        return smsRequest;
    }

    public ISupplierConfig getSmsConfig(SmsSupplier smsSupplier) {
        SupplierTypeEnum supplierType = SupplierTypeEnum.getSupplierType(smsSupplier.getSupplierType());
        Class<? extends ISupplierConfig> configClass = supplierType.getConfigClass();
        ISupplierConfig supplierConfig = BeanCopierUtils.copyProperties(supplierType, configClass);
        supplierConfig.setOperatorId(smsSupplier.getOperatorId());
        return supplierConfig;
        /*if (SupplierTypeEnum.ALIBABA.getCode().equals(smsSupplier.getSupplierType())) {
            AlibabaConfig alibabaConfig = AlibabaConfig.builder().build();
            alibabaConfig.setOperatorId(smsSupplier.getOperatorId());
            alibabaConfig.setRegionId(smsSupplier.getRegionId());
            alibabaConfig.setSignature(smsSupplier.getRegionId());
            alibabaConfig.setAccessKeyId(smsSupplier.getAccessKeyId());
            alibabaConfig.setAccessKeySecret(smsSupplier.getAccessKeySecret());
            return alibabaConfig;
        } else if (SupplierTypeEnum.JUHE.getCode().equals(smsSupplier.getSupplierType())) {
            JuheConfig juheConfig = JuheConfig.builder().build();
            juheConfig.setKey(smsSupplier.getKeyValue());
            juheConfig.setDomainAddress(smsSupplier.getDomainAddress());
            juheConfig.setOperatorId(smsSupplier.getOperatorId());
            return juheConfig;
        }
        return null;*/
    }

    @Override
    public void excelExport(RequestVO<SmsSupplierPageDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), getWrapper(requestVO.getT()), SmsSupplierPageVO.class, response);
    }
}
