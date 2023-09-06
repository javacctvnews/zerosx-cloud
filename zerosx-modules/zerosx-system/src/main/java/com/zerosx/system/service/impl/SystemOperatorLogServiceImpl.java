package com.zerosx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.utils.IpUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SystemOperatorLogDTO;
import com.zerosx.system.dto.SystemOperatorLogPageDTO;
import com.zerosx.system.entity.SystemOperatorLog;
import com.zerosx.system.mapper.ISystemOperatorLogMapper;
import com.zerosx.system.service.ISystemOperatorLogService;
import com.zerosx.system.vo.SystemOperatorLogPageVO;
import com.zerosx.system.vo.SystemOperatorLogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-02 15:06:36
 */
@Slf4j
@Service
public class SystemOperatorLogServiceImpl extends SuperServiceImpl<ISystemOperatorLogMapper, SystemOperatorLog> implements ISystemOperatorLogService {

    @Override
    public CustomPageVO<SystemOperatorLogPageVO> pageList(RequestVO<SystemOperatorLogPageDTO> requestVO, boolean searchCount) {
        LambdaQueryWrapper<SystemOperatorLog> wrapper = getWrapper(requestVO.getT());
        wrapper.select(SystemOperatorLog.class, log -> !log.getColumn().equals("operator_param") && !log.getColumn().equals("json_result") && !log.getColumn().equals("error_msg"));
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), wrapper), SystemOperatorLogPageVO.class);
    }

    @Override
    public boolean add(SystemOperatorLogDTO systemOperatorLogDTO) {
        SystemOperatorLog addEntity = BeanCopierUtil.copyProperties(systemOperatorLogDTO, SystemOperatorLog.class);
        addEntity.setIpLocation(IpUtils.getIpLocation(addEntity.getOperatorIp()));
        return save(addEntity);
    }

    @Override
    public boolean update(SystemOperatorLogDTO systemOperatorLogDTO) {
        SystemOperatorLog dbUpdate = getById(systemOperatorLogDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SystemOperatorLog updateEntity = BeanCopierUtil.copyProperties(systemOperatorLogDTO, SystemOperatorLog.class);
        return updateById(updateEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long[] id) {
        return removeByIds(Arrays.asList(id));
    }

    @Override
    public boolean deleteSystemOperatorLog(int deleteDays) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(deleteDays);
        Date deleteDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LambdaQueryWrapper<SystemOperatorLog> rmqw = Wrappers.lambdaQuery(SystemOperatorLog.class);
        rmqw.le(SystemOperatorLog::getOperatorTime, deleteDate);
        return remove(rmqw);
    }

    @Override
    public boolean cleanAll() {
        LambdaQueryWrapper<SystemOperatorLog> deqm = Wrappers.lambdaQuery(SystemOperatorLog.class);
        deqm.le(SystemOperatorLog::getCreateTime, LocalDateTime.now());
        return remove(deqm);
    }

    @Override
    public List<SystemOperatorLog> queryPageVOList(SystemOperatorLogPageDTO query) {
        return list(getWrapper(query));
    }

    @Override
    public SystemOperatorLogPageVO queryById(Long id) {
        return BeanCopierUtil.copyProperties(getById(id), SystemOperatorLogPageVO.class);
    }

    private LambdaQueryWrapper<SystemOperatorLog> getWrapper(SystemOperatorLogPageDTO query) {
        LambdaQueryWrapper<SystemOperatorLog> listqw = Wrappers.lambdaQuery(SystemOperatorLog.class);
        if (query == null) {
            return listqw;
        }
        listqw.like(StringUtils.isNotBlank(query.getTitle()), SystemOperatorLog::getTitle, query.getTitle());
        listqw.eq(StringUtils.isNotBlank(query.getOperatorName()), SystemOperatorLog::getOperatorName, query.getOperatorName());
        listqw.eq(query.getStatus() != null, SystemOperatorLog::getStatus, query.getStatus());
        listqw.eq(query.getBusinessType() != null, SystemOperatorLog::getBusinessType, query.getBusinessType());
        listqw.ge(StringUtils.isNotBlank(query.getBeginOperatorTime()), SystemOperatorLog::getOperatorTime, query.getBeginOperatorTime());
        listqw.le(StringUtils.isNotBlank(query.getEndOperatorTime()), SystemOperatorLog::getOperatorTime, query.getEndOperatorTime());
        //listqw.orderByDesc(SystemOperatorLog::getOperatorTime);
        return listqw;
    }
}
