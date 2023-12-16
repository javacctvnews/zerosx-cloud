package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.BaseTenantDTO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.system.dto.SysPostDTO;
import com.zerosx.system.dto.SysPostPageDTO;
import com.zerosx.system.service.ISysPostService;
import com.zerosx.system.vo.SysPostPageVO;
import com.zerosx.system.vo.SysPostVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 岗位管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-28 15:40:01
 */
@Slf4j
@RestController
@Tag(name = "岗位管理")
public class SysPostController {

    @Autowired
    private ISysPostService sysPostService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "岗位管理", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_post/page_list")
    public ResultVO<CustomPageVO<SysPostPageVO>> pageList(@RequestBody RequestVO<SysPostPageDTO> requestVO) {
        return ResultVOUtil.success(sysPostService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "岗位管理", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sys_post/save")
    public ResultVO<?> add(@Validated @RequestBody SysPostDTO sysPostDTO) {
        return ResultVOUtil.successBoolean(sysPostService.add(sysPostDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "岗位管理", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sys_post/update")
    public ResultVO<?> update(@Validated @RequestBody SysPostDTO sysPostDTO) {
        return ResultVOUtil.successBoolean(sysPostService.update(sysPostDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_post/queryById/{id}")
    public ResultVO<SysPostVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysPostService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "岗位管理", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sys_post/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(sysPostService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "岗位管理", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sys_post/export")
    public void operatorExport(@RequestBody RequestVO<SysPostPageDTO> requestVO, HttpServletResponse response) throws IOException {
        sysPostService.excelExport(requestVO, response);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/sys_post/select_list")
    public ResultVO<List<SelectOptionVO>> selectOptions(@RequestBody BaseTenantDTO baseTenantDTO) {
        return ResultVOUtil.success(sysPostService.selectOptions(baseTenantDTO));
    }
}
