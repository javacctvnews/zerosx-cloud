package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.common.log.vo.SystemOperatorLogBO;
import com.zerosx.common.utils.BeanCopierUtils;
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
    @OpLog(mod = "操作日志", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/system_operator_log/page_list")
    public ResultVO<CustomPageVO<SystemOperatorLogPageVO>> pageList(@RequestBody RequestVO<SystemOperatorLogPageDTO> requestVO) {
        return ResultVOUtil.success(systemOperatorLogService.pageList(requestVO, true));
    }

    @Operation(summary = "系统记录操作日志")
    //@SystemLog(title = "操作日志", businessType= BusinessType.INSERT)
    @PostMapping("/system_operator_log/save")
    public ResultVO<?> add(@Validated @RequestBody SystemOperatorLogBO systemOperatorLogBO) {
        SystemOperatorLogDTO systemOperatorLogDTO = BeanCopierUtils.copyProperties(systemOperatorLogBO, SystemOperatorLogDTO.class);
        return ResultVOUtil.successBoolean(systemOperatorLogService.add(systemOperatorLogDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "操作日志", btn = "删除", opType = OpTypeEnum.DELETE)
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
    @OpLog(mod = "操作日志", btn = "清空全部", opType = OpTypeEnum.DELETE)
    @PostMapping("/system_operator_log/clean")
    public ResultVO<?> cleanAll() {
        return ResultVOUtil.successBoolean(systemOperatorLogService.cleanAll());
    }

    @Operation(summary = "导出")
    @OpLog(mod = "操作日志", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/system_operator_log/export")
    public void operatorExport(@RequestBody RequestVO<SystemOperatorLogPageDTO> requestVO, HttpServletResponse response) throws IOException {
        systemOperatorLogService.excelExport(requestVO, response);
    }

}
