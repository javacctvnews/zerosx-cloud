package com.zerosx.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.easyexcel.AutoColumnWidthWriteHandler;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.easyexcel.XHorizontalCellStyleStrategy;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.service.impl.SuperServiceImpl;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.core.utils.PageUtils;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.IpUtils;
import com.zerosx.system.dto.SystemOperatorLogDTO;
import com.zerosx.system.dto.SystemOperatorLogPageDTO;
import com.zerosx.system.entity.SystemOperatorLog;
import com.zerosx.system.mapper.ISystemOperatorLogMapper;
import com.zerosx.system.service.ISystemOperatorLogService;
import com.zerosx.system.vo.SystemOperatorLogPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
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

    @Autowired
    private RedissonOpService redissonOpService;
    @Autowired
    private ISystemOperatorLogMapper systemOperatorLogMapper;

    @Override
    public CustomPageVO<SystemOperatorLogPageVO> pageList(RequestVO<SystemOperatorLogPageDTO> requestVO, boolean searchCount) {
        LambdaQueryWrapper<SystemOperatorLog> wrapper = getWrapper(requestVO.getT());
        wrapper.select(SystemOperatorLog.class, log -> !log.getColumn().equals("operator_param") && !log.getColumn().equals("json_result") && !log.getColumn().equals("error_msg"));
        return PageUtils.of(baseMapper.selectPage(PageUtils.of(requestVO, searchCount), wrapper), SystemOperatorLogPageVO.class);
    }

    @Override
    public boolean add(SystemOperatorLogDTO systemOperatorLogDTO) {
        SystemOperatorLog addEntity = BeanCopierUtils.copyProperties(systemOperatorLogDTO, SystemOperatorLog.class);
        addEntity.setIpLocation(IpUtils.getIpLocation(addEntity.getOperatorIp()));
        return save(addEntity);
    }

    @Override
    public boolean update(SystemOperatorLogDTO systemOperatorLogDTO) {
        SystemOperatorLog dbUpdate = getById(systemOperatorLogDTO.getId());
        if (dbUpdate == null) {
            throw new BusinessException("编辑记录不存在");
        }
        SystemOperatorLog updateEntity = BeanCopierUtils.copyProperties(systemOperatorLogDTO, SystemOperatorLog.class);
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
        return EasyTransUtils.copyTrans(getById(id), SystemOperatorLogPageVO.class);
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
        return listqw;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSystemOperatorLog() {
        double timePoint = (double) new Date().getTime();
        //读取大小
        int totalNum = redissonOpService.zLen(RedisKeyNameEnum.key(RedisKeyNameEnum.SYS_OP_LOG));
        if (totalNum == 0) {
            return;
        }
        long t1 = System.currentTimeMillis();
        int sheetNum = 1000;
        //按1000大小进行循环读取并保存
        int loopNum = (int) Math.ceil((double) totalNum / sheetNum);
        int offset = 0;
        for (int i = 0; i < loopNum; i++) {
            //获取
            log.debug("读取{}-{}", offset, sheetNum);
            Collection<SystemOperatorLogBO> collection = redissonOpService.zGetByScore(RedisKeyNameEnum.key(RedisKeyNameEnum.SYS_OP_LOG),
                    timePoint, offset, sheetNum);
            if (CollectionUtils.isEmpty(collection)) {
                break;
            }
            Collection<SystemOperatorLog> systemOperatorLogs = BeanCopierUtils.copyProperties(collection, SystemOperatorLog.class);
            for (SystemOperatorLog bo : systemOperatorLogs) {
                bo.setIpLocation(IpUtils.getIpLocation(bo.getOperatorIp()));
            }
            //保存
            boolean saveBatch = saveBatch(systemOperatorLogs);
            log.debug("第{}次批量保存条数:{}", i + 1, collection.size());
            offset += sheetNum;
        }
        //循环完删除
        int deleteNum = redissonOpService.zRemByScore(RedisKeyNameEnum.key(RedisKeyNameEnum.SYS_OP_LOG), timePoint, null);
        log.debug("批量保存日志{}条，耗时{}ms", deleteNum, System.currentTimeMillis() - t1);
    }

    @Override
    public void excelExport(RequestVO<SystemOperatorLogPageDTO> requestVO, HttpServletResponse response) {
        checkEasyExcelProperties();
        long t1 = System.currentTimeMillis();
        long totalCount = systemOperatorLogMapper.queryExportCount(requestVO.getT());
        log.debug("查询总数{}条，耗时{}ms", totalCount, System.currentTimeMillis() - t1);
        if (totalCount <= 0) {
            return;
        }
        Integer sheetNum = easyExcelProperties.getSheetNum();
        Integer querySize = easyExcelProperties.getQuerySize();
        //每个sheet最大查询次数
        int count = sheetNum / querySize;
        //多少个sheet页
        int sheetLoop = (int) Math.ceil((double) totalCount / sheetNum);
        //已查询条数
        int queryCurrentCount = 0;
        List<SystemOperatorLog> records;
        SystemOperatorLogPageDTO pageDTO = requestVO.getT();
        try (ExcelWriter excelWriter = EasyExcel.write(EasyExcelUtil.getOutputStream(response), SystemOperatorLogPageVO.class).build()) {
            //循环写入sheet页数据
            for (int i = 0; i < sheetLoop; i++) {
                if (queryCurrentCount == totalCount) {
                    break;
                }
                //创建WriteSheet
                WriteSheet writeSheet = EasyExcel.writerSheet("Sheet" + (i + 1))
                        .registerWriteHandler(new AutoColumnWidthWriteHandler())
                        .registerWriteHandler(new XHorizontalCellStyleStrategy()).build();
                for (int j1 = 0; j1 < count; j1++) {
                    if (queryCurrentCount == totalCount) {
                        break;
                    }
                    int pageNum = (i * count) + (j1 + 1);
                    long t11 = System.currentTimeMillis();
                    pageDTO.setLimitNumber(querySize);
                    pageDTO.setBeginLimit((pageNum - 1) * querySize);
                    records = baseMapper.queryExportPage(requestVO.getT());
                    int size = records.size();
                    queryCurrentCount += size;
                    log.debug("第{}页查询，每页{}条 实际{}条，耗时{}ms 已查询{}条", pageNum, querySize, size, System.currentTimeMillis() - t11, queryCurrentCount);
                    if (size > 0) {
                        excelWriter.write(EasyTransUtils.copyTrans(records, SystemOperatorLogPageVO.class), writeSheet);
                    }
                }
            }
        }
        log.debug("【{}】执行导出{}条，总耗时:{}ms", ZerosSecurityContextHolder.getUserName(), totalCount, System.currentTimeMillis() - t1);
    }
}
