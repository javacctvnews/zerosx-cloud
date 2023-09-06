package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SysDeptDTO;
import com.zerosx.system.dto.SysDeptPageDTO;
import com.zerosx.system.entity.SysDept;
import com.zerosx.system.service.ISysDeptService;
import com.zerosx.system.vo.SysDeptPageVO;
import com.zerosx.system.vo.SysDeptVO;
import com.zerosx.system.vo.SysTreeSelectVO;
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
 * 部门管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-29 17:42:27
 */
@Slf4j
@RestController
@Tag(name = "部门管理")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "部门管理", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/sys_dept/page_list")
    public ResultVO<CustomPageVO<SysDeptPageVO>> pageList(@RequestBody RequestVO<SysDeptPageDTO> requestVO) {
        return ResultVOUtil.success(sysDeptService.pageList(requestVO, true));
    }

    @Operation(summary = "分页列表")
    @SystemLog(title = "部门管理", btnName = "表格树形结构", businessType = BusinessType.QUERY)
    @PostMapping("/sys_dept/table_tree")
    public ResultVO<List<SysDept>> tableTree(@RequestBody SysDeptPageDTO sysDeptPageDTO) {
        return ResultVOUtil.success(sysDeptService.tableTree(sysDeptPageDTO));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "部门管理", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/sys_dept/save")
    public ResultVO<?> add(@Validated @RequestBody SysDeptDTO sysDeptDTO) {
        return ResultVOUtil.successBoolean(sysDeptService.add(sysDeptDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "部门管理", btnName = "编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/sys_dept/update")
    public ResultVO<?> update(@Validated @RequestBody SysDeptDTO sysDeptDTO) {
        return ResultVOUtil.successBoolean(sysDeptService.update(sysDeptDTO));
    }

    @Operation(summary = "按id查询")
    @SystemLog(title = "部门管理", btnName = "按id查询", businessType = BusinessType.QUERY)
    @GetMapping("/sys_dept/queryById/{id}")
    public ResultVO<SysDeptVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysDeptService.queryById(id));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "部门管理", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys_dept/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(sysDeptService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "部门管理", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sys_dept/export")
    public void operatorExport(@RequestBody RequestVO<SysDeptPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysDeptPageVO> data = sysDeptService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, data.getList(), SysDeptPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), data.getTotal(), System.currentTimeMillis() - t1);
    }


    @Operation(summary = "删除")
    @PostMapping("/sys_dept/tree_select")
    public ResultVO<List<SysTreeSelectVO>> treeSelect() {
        return ResultVOUtil.success(sysDeptService.treeSelect());
    }

}
