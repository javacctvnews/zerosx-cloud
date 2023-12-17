package com.zerosx.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.system.vo.MutiTenancyGroupBO;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.enums.StatusEnum;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.ds.constant.DSType;
import com.zerosx.system.dto.MutiTenancyGroupEditDTO;
import com.zerosx.system.dto.MutiTenancyGroupQueryDTO;
import com.zerosx.system.dto.MutiTenancyGroupSaveDTO;
import com.zerosx.system.entity.MutiTenancyGroup;
import com.zerosx.system.mapper.IMutiTenancyGroupMapper;
import com.zerosx.system.service.IMutiTenancyGroupService;
import com.zerosx.system.task.SystemAsyncTask;
import com.zerosx.system.vo.MutiTenancyGroupPageVO;
import com.zerosx.system.vo.MutiTenancyGroupVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName MutiTenancyGroupServiceImpl
 * @Description 租户集团公司实现
 * @Author javacctvnews
 * @Date 2023/3/13 10:45
 * @Version 1.0
 */
@Slf4j
@Service
public class MutiTenancyGroupServiceImpl extends SuperServiceImpl<IMutiTenancyGroupMapper, MutiTenancyGroup> implements IMutiTenancyGroupService {

    @Autowired
    private IMutiTenancyGroupMapper mutiTenancyGroupMapper;
    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private SystemAsyncTask systemAsyncTask;

    @Override
    @DS(DSType.SLAVE)
    public CustomPageVO<MutiTenancyGroupPageVO> listPages(RequestVO<MutiTenancyGroupQueryDTO> requestVO, boolean searchCount) {
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), lambdaQW(requestVO.getT())), MutiTenancyGroupPageVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMutiTenancyGroup(MutiTenancyGroupSaveDTO mutiTenancyGroupSaveDTO) {
        //随机生成租户ID 6位长度
        String tenantId = IdGenerator.getRandomStr(6);
        MutiTenancyGroup mutiTenancyGroup = BeanCopierUtils.copyProperties(mutiTenancyGroupSaveDTO, MutiTenancyGroup.class);
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
    @Transactional(rollbackFor = Exception.class)
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
        MutiTenancyGroup mutiTenancyGroup = BeanCopierUtils.copyProperties(editDTO, MutiTenancyGroup.class);
        redissonOpService.hDel(ZCache.OPERATOR.key(dbGroup.getOperatorId()));
        boolean b = updateById(mutiTenancyGroup);
        if (b) {
            systemAsyncTask.asyncHRemove(ZCache.OPERATOR.key(dbGroup.getOperatorId()), null);
        }
        return b;
    }

    @Override
    @DS(DSType.SLAVE)
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
    @DS(DSType.SLAVE)
    public List<MutiTenancyGroup> listData(MutiTenancyGroupQueryDTO query) {
        return list(lambdaQW(query));
    }

    @Override
    @DS(DSType.SLAVE)
    public MutiTenancyGroupVO getTenantById(Long id) {
        MutiTenancyGroup record = getById(id);
        return EasyTransUtils.copyTrans(record, MutiTenancyGroupVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupCompany(Long[] ids) {
        for (Long id : ids) {
            removeById(id);
        }
        return true;
    }

    @Override
    @DS(DSType.SLAVE)
    public String transIdName(String operatorId) {
        if (StringUtils.isBlank(operatorId)) {
            return StringUtils.EMPTY;
        }
        String cacheName = StringUtils.EMPTY;
        MutiTenancyGroup mutiTenancyGroup = baseMapper.selectTenancy(operatorId);
        if (mutiTenancyGroup != null) {
            cacheName = mutiTenancyGroup.getTenantGroupName();
        }
        redissonOpService.hPut(ZCache.OPERATOR.key(operatorId), TranslConstants.OPERATOR_NAME, cacheName);
        return cacheName;
    }

    @Override
    public MutiTenancyGroupBO queryOperator(String operatorId) {
        if (StringUtils.isBlank(operatorId)) {
            return null;
        }
        MutiTenancyGroup mutiTenancyGroup = baseMapper.selectTenancy(operatorId);
        if (mutiTenancyGroup == null) {
            return null;
        }
        MutiTenancyGroupBO mutiTenancyGroupBO = BeanCopierUtils.copyProperties(mutiTenancyGroup, MutiTenancyGroupBO.class);
        Map<String, Object> map = JacksonUtil.toMap(mutiTenancyGroupBO);
        redissonOpService.hPut(ZCache.OPERATOR.key(operatorId), map);
        return mutiTenancyGroupBO;
    }

    @Override
    public void excelExport(RequestVO<MutiTenancyGroupQueryDTO> requestVO, HttpServletResponse response) {
        excelExport(PageUtils.of(requestVO, false), lambdaQW(requestVO.getT()), MutiTenancyGroupPageVO.class, response);
    }
}
