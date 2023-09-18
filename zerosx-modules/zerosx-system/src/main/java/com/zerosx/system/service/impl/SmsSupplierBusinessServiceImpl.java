package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.enums.sms.SmsBusinessCodeEnum;
import com.zerosx.system.dto.SmsSupplierBusinessDTO;
import com.zerosx.system.dto.SmsSupplierBusinessPageDTO;
import com.zerosx.system.entity.SmsSupplierBusiness;
import com.zerosx.system.mapper.ISmsSupplierBusinessMapper;
import com.zerosx.system.service.ISmsSupplierBusinessService;
import com.zerosx.system.vo.SmsSupplierBusinessPageVO;
import com.zerosx.system.vo.SmsSupplierBusinessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 短信业务模板
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-31 10:39:49
 */
@Slf4j
@Service
public class SmsSupplierBusinessServiceImpl extends SuperServiceImpl<ISmsSupplierBusinessMapper, SmsSupplierBusiness> implements ISmsSupplierBusinessService {

    @Override
    public CustomPageVO<SmsSupplierBusinessPageVO> pageList(RequestVO<SmsSupplierBusinessPageDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), getWrapper(requestVO.getT())), SmsSupplierBusinessPageVO.class);
    }

    @Override
    public List<SmsSupplierBusiness> dataList(SmsSupplierBusinessPageDTO query) {
        return list(getWrapper(query));
    }

    private LambdaQueryWrapper<SmsSupplierBusiness> getWrapper(SmsSupplierBusinessPageDTO query) {
        LambdaQueryWrapper<SmsSupplierBusiness> qw = Wrappers.lambdaQuery(SmsSupplierBusiness.class);
        if (query == null) {
            return qw;
        }
        qw.eq(query.getSmsSupplierId() != null, SmsSupplierBusiness::getSmsSupplierId, query.getSmsSupplierId());
        return qw;
    }

    @Override
    public boolean add(SmsSupplierBusinessDTO businessDTO) {
        SmsSupplierBusiness addEntity = BeanCopierUtil.copyProperties(businessDTO, SmsSupplierBusiness.class);
        checkSmsSupplierBusiness(businessDTO);
        return save(addEntity);
    }

    private void checkSmsSupplierBusiness(SmsSupplierBusinessDTO businessDTO) {
        LambdaQueryWrapper<SmsSupplierBusiness> qw = Wrappers.lambdaQuery(SmsSupplierBusiness.class);
        qw.eq(SmsSupplierBusiness::getSmsSupplierId, businessDTO.getSmsSupplierId());
        qw.eq(SmsSupplierBusiness::getBusinessCode, businessDTO.getBusinessCode());
        long count = count(qw);
        if (count > 0) {
            throw new BusinessException("已存在的短信业务编码：" + SmsBusinessCodeEnum.getByCode(businessDTO.getBusinessCode()));
        }
    }

    @Override
    public boolean update(SmsSupplierBusinessDTO smsSupplierBusinessDTO) {
        SmsSupplierBusiness dbUpdate = getById(smsSupplierBusinessDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        if (!dbUpdate.getBusinessCode().equals(smsSupplierBusinessDTO.getBusinessCode())) {
            checkSmsSupplierBusiness(smsSupplierBusinessDTO);
        }
        SmsSupplierBusiness updateEntity = BeanCopierUtil.copyProperties(smsSupplierBusinessDTO, SmsSupplierBusiness.class);
        return updateById(updateEntity);
    }

    @Override
    public SmsSupplierBusinessVO queryById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), SmsSupplierBusinessVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

}
