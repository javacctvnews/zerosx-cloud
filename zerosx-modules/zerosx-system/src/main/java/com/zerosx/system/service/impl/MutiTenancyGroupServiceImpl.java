package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.system.dto.MutiTenancyGroupEditDTO;
import com.zerosx.system.dto.MutiTenancyGroupQueryDTO;
import com.zerosx.system.dto.MutiTenancyGroupSaveDTO;
import com.zerosx.system.entity.MutiTenancyGroup;
import com.zerosx.system.mapper.IMutiTenancyGroupMapper;
import com.zerosx.system.service.IMutiTenancyGroupService;
import com.zerosx.system.vo.MutiTenancyGroupPageVO;
import com.zerosx.system.vo.MutiTenancyGroupVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName MutiTenancyGroupServiceImpl
 * @Description 租户集团公司实现
 * @Author javacctvnews
 * @Date 2023/3/13 10:45
 * @Version 1.0
 */
@Service
public class MutiTenancyGroupServiceImpl extends SuperServiceImpl<IMutiTenancyGroupMapper, MutiTenancyGroup> implements IMutiTenancyGroupService {

    @Autowired
    private IMutiTenancyGroupMapper mutiTenancyGroupMapper;
    @Autowired
    private RedissonOpService redissonOpService;

    @Override
    public CustomPageVO<MutiTenancyGroupPageVO> listPages(RequestVO<MutiTenancyGroupQueryDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), lambdaQW(requestVO.getT())), MutiTenancyGroupPageVO.class);
    }

    @Override
    public boolean saveMutiTenancyGroup(MutiTenancyGroupSaveDTO mutiTenancyGroupSaveDTO) {
        //随机生成租户ID 6位长度
        String tenantId = IdGenerator.getRandomStr(6);
        MutiTenancyGroup mutiTenancyGroup = BeanCopierUtil.copyProperties(mutiTenancyGroupSaveDTO, MutiTenancyGroup.class);
        mutiTenancyGroup.setOperatorId(tenantId);
        mutiTenancyGroup.setAuditStatus(1);
        //公司名称或社会信用代码存在时不允许保存
        int count = mutiTenancyGroupMapper.selectTenancyExist(null, mutiTenancyGroupSaveDTO.getTenantGroupName(), mutiTenancyGroupSaveDTO.getSocialCreditCode());
        if (count > 0) {
            throw new BusinessException(String.format("已存在租户集团，公司全称：%s，社会信用代码：%s", mutiTenancyGroupSaveDTO.getTenantGroupName(), mutiTenancyGroupSaveDTO.getSocialCreditCode()));
        }
        return save(mutiTenancyGroup);
    }

    @Override
    public boolean editMutiTenancyGroup(MutiTenancyGroupEditDTO editDTO) {
        MutiTenancyGroup dbGroup = getById(editDTO.getId());
        if (dbGroup == null) {
            throw new BusinessException("不存在此租户集团");
        }
        //更新了公司名称或组织机构代码
        if (!dbGroup.getTenantGroupName().equals(editDTO.getTenantGroupName()) || !dbGroup.getSocialCreditCode().equals(editDTO.getSocialCreditCode())) {
            int count = mutiTenancyGroupMapper.selectTenancyExist(dbGroup.getId(), editDTO.getTenantGroupName(), editDTO.getSocialCreditCode());
            if (count > 0) {
                throw new BusinessException(String.format("已存在租户集团，公司全称：%s，社会信用代码：%s", editDTO.getTenantGroupName(), editDTO.getSocialCreditCode()));
            }
        }
        MutiTenancyGroup mutiTenancyGroup = BeanCopierUtil.copyProperties(editDTO, MutiTenancyGroup.class);
        redissonOpService.hRemove(RedisKeyNameEnum.key(RedisKeyNameEnum.OPERATOR, dbGroup.getOperatorId()), "operatorId");
        return updateById(mutiTenancyGroup);
    }

    @Override
    public List<SelectOptionVO> selectOptions() {
        LambdaQueryWrapper<MutiTenancyGroup> listAllQw = Wrappers.lambdaQuery(MutiTenancyGroup.class);
        listAllQw.eq(MutiTenancyGroup::getValidStatus, StatusEnum.NORMAL.getCode());
        List<MutiTenancyGroup> list = list(listAllQw);
        List<SelectOptionVO> tenancyList = new ArrayList<>();
        SelectOptionVO optionVO;
        for (MutiTenancyGroup mutiTenancyGroup : list) {
            optionVO = new SelectOptionVO();
            optionVO.setValue(mutiTenancyGroup.getOperatorId());
            optionVO.setLabel(mutiTenancyGroup.getTenantGroupName());
            tenancyList.add(optionVO);
        }
        //中文首字母排序
        if (!CollectionUtils.isEmpty(list)) {
            Collator collator = Collator.getInstance(Locale.CHINA);
            tenancyList.sort((o1, o2) -> collator.compare(o1.getLabel(), o2.getLabel()));
        }
        return tenancyList;
    }

    @NotNull
    public LambdaQueryWrapper<MutiTenancyGroup> lambdaQW(MutiTenancyGroupQueryDTO query) {
        LambdaQueryWrapper<MutiTenancyGroup> listqw = Wrappers.lambdaQuery(MutiTenancyGroup.class);
        listqw.eq(StringUtils.isNotBlank(query.getOperatorId()), MutiTenancyGroup::getOperatorId, query.getOperatorId());
        listqw.like(StringUtils.isNotBlank(query.getTenantGroupName()), MutiTenancyGroup::getTenantGroupName, query.getTenantGroupName());
        listqw.like(StringUtils.isNotBlank(query.getSocialCreditCode()), MutiTenancyGroup::getSocialCreditCode, query.getSocialCreditCode());
        listqw.like(StringUtils.isNotBlank(query.getContactName()), MutiTenancyGroup::getContactName, query.getContactName());
        listqw.eq(StringUtils.isNotBlank(query.getContactMobilePhone()), MutiTenancyGroup::getContactMobilePhone, query.getContactMobilePhone());
        return listqw;
    }

    @Override
    public List<MutiTenancyGroup> listData(MutiTenancyGroupQueryDTO query) {
        return list(lambdaQW(query));
    }

    @Override
    public MutiTenancyGroupVO getTenantById(Long id) {
        MutiTenancyGroup record = getById(id);
        return BeanCopierUtil.copyProperties(record, MutiTenancyGroupVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupCompany(Long[] ids) {
        return removeByIds(Arrays.asList(ids));
    }

    @Override
    public String transIdName(String operatorId) {
        if (StringUtils.isBlank(operatorId)) {
            return StringUtils.EMPTY;
        }
        MutiTenancyGroup mutiTenancyGroup = baseMapper.selectTenancy(operatorId);
        if (mutiTenancyGroup == null) {
            return StringUtils.EMPTY;
        }
        redissonOpService.hPut(RedisKeyNameEnum.key(RedisKeyNameEnum.OPERATOR, operatorId), "operatorId", mutiTenancyGroup.getTenantGroupName());
        return mutiTenancyGroup.getTenantGroupName();
    }
}
