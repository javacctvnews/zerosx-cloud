package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.system.dto.SystemOperatorLogDTO;
import com.zerosx.system.dto.SystemOperatorLogPageDTO;
import com.zerosx.system.service.ISystemOperatorLogService;
import com.zerosx.system.vo.SystemOperatorLogPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 操作日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-02 15:06:36
 */
@Slf4j
@RestController
@Tag(name = "操作日志")
public class SystemOperatorLogController {

    @Autowired
    private ISystemOperatorLogService systemOperatorLogService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "操作日志", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/system_operator_log/page_list")
    public ResultVO<CustomPageVO<SystemOperatorLogPageVO>> pageList(@RequestBody RequestVO<SystemOperatorLogPageDTO> requestVO) {
        return ResultVOUtil.success(systemOperatorLogService.pageList(requestVO, true));
    }

    @Operation(summary = "系统记录操作日志")
    //@SystemLog(title = "操作日志", businessType= BusinessType.INSERT)
    @PostMapping("/system_operator_log/save")
    public ResultVO<?> add(@Validated @RequestBody SystemOperatorLogBO systemOperatorLogBO) {
        SystemOperatorLogDTO systemOperatorLogDTO = BeanCopierUtil.copyProperties(systemOperatorLogBO, SystemOperatorLogDTO.class);
        return ResultVOUtil.successBoolean(systemOperatorLogService.add(systemOperatorLogDTO));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "操作日志", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/system_operator_log/delete/{id}")
    public ResultVO<?> deleteRecord(@PathVariable("id") Long[] id) {
        return ResultVOUtil.successBoolean(systemOperatorLogService.deleteRecord(id));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/system_operator_log/queryById/{id}")
    public ResultVO<SystemOperatorLogPageVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(systemOperatorLogService.queryById(id));
    }

    @Operation(summary = "清空全部")
    @SystemLog(title = "操作日志", btnName = "清空全部", businessType = BusinessType.DELETE)
    @PostMapping("/system_operator_log/clean")
    public ResultVO<?> cleanAll() {
        return ResultVOUtil.successBoolean(systemOperatorLogService.cleanAll());
    }

    @Operation(summary = "导出")
    @SystemLog(title = "操作日志", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/system_operator_log/export")
    public void operatorExport(@RequestBody RequestVO<SystemOperatorLogPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SystemOperatorLogPageVO> pages = systemOperatorLogService.pageList(requestVO, false);
        log.debug("【{}】执行导出 【查询、拷贝、翻译】耗时{}ms", ZerosSecurityContextHolder.getUserName(), System.currentTimeMillis() - t1);
        long t2 = System.currentTimeMillis();
        EasyExcelUtil.writeExcel(response, pages.getList(), SystemOperatorLogPageVO.class);
        log.debug("【{}】执行导出 {}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t2);
    }

}
