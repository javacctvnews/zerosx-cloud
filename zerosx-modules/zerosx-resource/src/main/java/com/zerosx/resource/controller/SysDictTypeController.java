package com.zerosx.resource.controller;


import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.SysDictTypeDTO;
import com.zerosx.resource.dto.SysDictTypeRetrieveDTO;
import com.zerosx.resource.dto.SysDictTypeUpdateDTO;
import com.zerosx.resource.service.ISysDictTypeService;
import com.zerosx.resource.vo.SysDictTypeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
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
    @OpLog(mod = "字典类型", btn = "新增", opType = OpTypeEnum.INSERT)
    public ResultVO<?> insertSysDictType(@Validated @RequestBody SysDictTypeDTO sysDictType) {
        return ResultVOUtil.success(sysDictTypeService.saveDictType(sysDictType));
    }

    @Operation(summary = "编辑")
    @PutMapping(value = "/sysDictType_update")
    @OpLog(mod = "字典类型", btn = "编辑", opType = OpTypeEnum.UPDATE)
    public ResultVO<?> updateSysDictType(@Validated @RequestBody SysDictTypeUpdateDTO sysDictTypeUpdateDTO) {
        return ResultVOUtil.success(sysDictTypeService.updateSysDictType(sysDictTypeUpdateDTO));
    }

    @Operation(summary = "分页查询")
    @PostMapping(value = "/sysDictType_page")
    @OpLog(mod = "字典类型", btn = "分页查询", opType = OpTypeEnum.QUERY)
    public ResultVO<CustomPageVO<SysDictTypeVO>> selectBySysDictTypePage(@RequestBody RequestVO<SysDictTypeRetrieveDTO> requestVO) {
        return ResultVOUtil.success(sysDictTypeService.pageList(requestVO, true));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "字典类型", btn = "删除", opType = OpTypeEnum.DELETE)
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
    @GetMapping(value = "/sysDictType/queryById/{dictId}")
    public ResultVO<SysDictTypeVO> getDictTypeById(@PathVariable("dictId") Long dictId) {
        return ResultVOUtil.success(sysDictTypeService.getDictTypeById(dictId));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "字典类型", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sysDictType/export")
    public void operatorExport(@RequestBody RequestVO<SysDictTypeRetrieveDTO> requestVO, HttpServletResponse response) throws IOException {
        sysDictTypeService.excelExport(requestVO, response);
    }
}
