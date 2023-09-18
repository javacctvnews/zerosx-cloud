package com.zerosx.system.controller;


import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SysDictTypeDTO;
import com.zerosx.system.dto.SysDictTypeRetrieveDTO;
import com.zerosx.system.dto.SysDictTypeUpdateDTO;
import com.zerosx.system.service.ISysDictTypeService;
import com.zerosx.system.vo.SysDictTypeVO;
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
 * 字典类型表 前端控制器
 */
@Tag(name = "字典类型")
@RestController
@Slf4j
public class SysDictTypeController {

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Operation(summary = "新增")
    @PostMapping(value = "/sysDictType_insert")
    @SystemLog(title = "字典类型", btnName = "新增", businessType = BusinessType.INSERT)
    public ResultVO<?> insertSysDictType(@Validated @RequestBody SysDictTypeDTO sysDictType) {
        return ResultVOUtil.success(sysDictTypeService.saveDictType(sysDictType));
    }

    @Operation(summary = "编辑")
    @PutMapping(value = "/sysDictType_update")
    @SystemLog(title = "字典类型", btnName = "编辑", businessType = BusinessType.UPDATE)
    public ResultVO<?> updateSysDictType(@Validated @RequestBody SysDictTypeUpdateDTO sysDictTypeUpdateDTO) {
        return ResultVOUtil.success(sysDictTypeService.updateSysDictType(sysDictTypeUpdateDTO));
    }

    @Operation(summary = "分页查询")
    @PostMapping(value = "/sysDictType_page")
    @SystemLog(title = "字典类型", btnName = "分页查询", businessType = BusinessType.QUERY)
    public ResultVO<CustomPageVO<SysDictTypeVO>> selectBySysDictTypePage(@RequestBody RequestVO<SysDictTypeRetrieveDTO> requestVO) {
        return ResultVOUtil.success(sysDictTypeService.pageList(requestVO, true));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "字典类型", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/sysDictType_delete/{dictId}")
    public ResultVO<?> deleteDictType(@PathVariable("dictId") Long[] dictId) {
        return ResultVOUtil.success(sysDictTypeService.deleteDictType(dictId));
    }

    @Operation(summary = "按条件查询")
    @PostMapping(value = "/sysDictType_list")
    public ResultVO<List<SysDictTypeVO>> getSysDictTypeList(@RequestBody SysDictTypeRetrieveDTO sysDictType) {
        return ResultVOUtil.success(sysDictTypeService.selectByMap(sysDictType));
    }

    @Operation(summary = "按id查询")
    @GetMapping(value = "/getDictTypeById/{dictId}")
    public ResultVO<SysDictTypeVO> getDictTypeById(@PathVariable("dictId") Long dictId) {
        return ResultVOUtil.success(sysDictTypeService.getDictTypeById(dictId));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "字典类型", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sysDictType/export")
    public void operatorExport(@RequestBody RequestVO<SysDictTypeRetrieveDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysDictTypeVO> pages = sysDictTypeService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), SysDictTypeVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }
}
