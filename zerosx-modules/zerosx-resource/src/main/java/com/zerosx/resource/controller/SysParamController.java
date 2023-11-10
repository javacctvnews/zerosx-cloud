package com.zerosx.resource.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.SysParamDTO;
import com.zerosx.resource.dto.SysParamPageDTO;
import com.zerosx.resource.service.ISysParamService;
import com.zerosx.resource.vo.SysParamPageVO;
import com.zerosx.resource.vo.SysParamVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统参数
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-29 01:02:29
 */
@Slf4j
@RestController
@Tag(name = "系统参数")
public class SysParamController {

    @Autowired
    private ISysParamService sysParamService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "系统参数", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_param/page_list")
    public ResultVO<CustomPageVO<SysParamPageVO>> pageList(@RequestBody RequestVO<SysParamPageDTO> requestVO) {
        return ResultVOUtil.success(sysParamService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "系统参数", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sys_param/save")
    public ResultVO<?> add(@Validated @RequestBody SysParamDTO sysParamDTO) {
        return ResultVOUtil.successBoolean(sysParamService.add(sysParamDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "系统参数", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sys_param/update")
    public ResultVO<?> update(@Validated @RequestBody SysParamDTO sysParamDTO) {
        return ResultVOUtil.successBoolean(sysParamService.update(sysParamDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_param/queryById/{id}")
    public ResultVO<SysParamVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysParamService.queryById(id));
    }

    @Operation(summary = "按编码查询")
    @PostMapping("/sys_param/queryByKey")
    public ResultVO<SysParamVO> queryByKey(@RequestBody SysParamPageDTO sysParamPageDTO) {
        return ResultVOUtil.success(sysParamService.queryByKey(sysParamPageDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "系统参数", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sys_param/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(sysParamService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "系统参数", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sys_param/export")
    public void operatorExport(@RequestBody RequestVO<SysParamPageDTO> requestVO, HttpServletResponse response) throws IOException {
        sysParamService.excelExport(requestVO, response);
    }

}
