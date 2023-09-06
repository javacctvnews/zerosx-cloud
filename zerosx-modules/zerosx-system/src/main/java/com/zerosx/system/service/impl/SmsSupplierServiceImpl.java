package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.system.dto.SmsSendDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.sms.core.SmsFactory;
import com.zerosx.sms.core.config.AlibabaConfig;
import com.zerosx.sms.core.config.ISupplierConfig;
import com.zerosx.sms.core.config.JuheConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import com.zerosx.sms.model.SmsRequest;
import com.zerosx.sms.model.SmsResponse;
import com.zerosx.sms.properties.CustomSmsProperties;
import com.zerosx.system.dto.SmsSupplierDTO;
import com.zerosx.system.dto.SmsSupplierPageDTO;
import com.zerosx.system.entity.SmsSupplier;
import com.zerosx.system.entity.SmsSupplierBusiness;
import com.zerosx.system.mapper.ISmsSupplierMapper;
import com.zerosx.system.service.ISmsSupplierBusinessService;
import com.zerosx.system.service.ISmsSupplierService;
import com.zerosx.system.vo.SmsSupplierPageVO;
import com.zerosx.system.vo.SmsSupplierVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        SmsSupplier addEntity = BeanCopierUtil.copyProperties(smsSupplierDTO, SmsSupplier.class);
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
        SmsSupplier updateEntity = BeanCopierUtil.copyProperties(smsSupplierDTO, SmsSupplier.class);
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
        return BeanCopierUtil.copyProperties(getById(id), SmsSupplierVO.class);
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
        smsRequest = new SmsRequest();
        smsRequest.setSignature(StringUtils.isBlank(ssb.getSignature()) ? supplier.getSignature() : ssb.getSignature());
        smsRequest.setTemplateParam(JacksonUtil.toJSONString(smsSendDTO.getParams()));
        smsRequest.setTemplateCode(ssb.getTemplateCode());
        smsRequest.setPhoneNumbers(smsSendDTO.getPhoneNumbers());
        smsRequest.setOperatorId(smsSendDTO.getOperatorId());

        if (smsConfig == null) {
            throw new BusinessException("【ISupplierConfig】配置为空");
        }
        SmsResponse smsResponse = SmsFactory.createSmsClient(SupplierTypeEnum.getSupplierType(supplierType), smsConfig)
                .sendMessage(smsRequest);
        if (smsResponse.getSendCode() == 0) {
            return ResultVOUtil.success(smsResponse);
        }
        return ResultVOUtil.error(smsResponse.getSendCode(), smsResponse.getSendMsg());
    }

    public ISupplierConfig getSmsConfig(SmsSupplier smsSupplier) {
        if (SupplierTypeEnum.ALIBABA.getCode().equals(smsSupplier.getSupplierType())) {
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
        return null;
    }
}
