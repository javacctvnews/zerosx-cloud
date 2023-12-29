package com.zerosx.resource.controller;


import com.zerosx.api.system.dto.SysDictDataPageDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.enums.IdempotentTypeEnum;
import com.zerosx.resource.dto.SysDictDataDTO;
import com.zerosx.resource.dto.SysDictDataUpdateDTO;
import com.zerosx.resource.service.ISysDictDataService;
import com.zerosx.resource.vo.SysDictDataVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 字典数据表 前端控制器
 */
@Tag(name = "字典数据")
@RestController
@Slf4j
public class SysDictDataController {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Operation(summary = "分页查询")
    @PostMapping(value = "/sysDictData_page")
    @OpLog(mod = "字典数据", btn = "分页查询", opType = OpTypeEnum.QUERY)
    public ResultVO<CustomPageVO<SysDictDataVO>> selectBySysDictDataPage(@RequestBody RequestVO<SysDictDataPageDTO> sysDictData) {
        return ResultVOUtil.success(sysDictDataService.pageList(sysDictData, true));
    }

    @Operation(summary = "新增")
    @PostMapping(value = "/sysDictData_insert")
    @OpLog(mod = "字典数据", btn = "新增", opType = OpTypeEnum.INSERT)
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "#sysDictDataDTO.dictType+'_'+#sysDictDataDTO.dictValue")
    public ResultVO<?> insert(@RequestBody @Validated SysDictDataDTO sysDictDataDTO) {
        return ResultVOUtil.successBoolean(sysDictDataService.insert(sysDictDataDTO));
    }

    @Operation(summary = "编辑")
    @PutMapping(value = "/sysDictData_update")
    @OpLog(mod = "字典数据-编辑", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @Idempotent(type = IdempotentTypeEnum.SPEL, spEL = "#sysDictDataUpdateDTO.dictType+'_'+#sysDictDataUpdateDTO.dictValue")
    public ResultVO<?> update(@RequestBody @Validated SysDictDataUpdateDTO sysDictDataUpdateDTO) {
        return ResultVOUtil.successBoolean(sysDictDataService.update(sysDictDataUpdateDTO));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "字典数据", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping(value = "/sysDictData_delete/{dictCode}")
    public ResultVO<?> deleteSysDictData(@PathVariable("dictCode") Long[] dictCode) {
        return ResultVOUtil.successBoolean(sysDictDataService.deleteSysDictData(dictCode));
    }

    @Operation(summary = "查询指定字典类型的List集合")
    @GetMapping(value = "/sysDictData_selectList/{dictType}")
    public ResultVO<List<I18nSelectOptionVO>> getDictList(@PathVariable("dictType") String dictType) {
        return ResultVOUtil.success(sysDictDataService.getDictList(dictType));
    }

    @Operation(summary = "查询字典类型的字典数据")
    @GetMapping(value = "/sysDictData/{dictType}/type")
    public ResultVO<Map<Object, String>> getDictData(@PathVariable("dictType") String dictType) {
        return ResultVOUtil.success(sysDictDataService.getDictDataMap(dictType));
    }

    @Operation(summary = "刷新缓存")
    @OpLog(mod = "字典数据", btn = "刷新缓存", opType = OpTypeEnum.UPDATE)
    @GetMapping(value = "/sysDictData/init")
    public ResultVO<?> initDictData(@RequestParam(value = "dictType", required = false) String dictType) {
        sysDictDataService.initCacheDictData(dictType);
        return ResultVOUtil.success();
    }

    @Operation(summary = "按id查询字典详情")
    @GetMapping(value = "/sysDictData/queryById/{id}")
    public ResultVO<SysDictDataVO> getDictById(@PathVariable("id") Long id) {
        return ResultVOUtil.success(sysDictDataService.getDictById(id));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "字典数据", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sysDictData/export")
    public void operatorExport(@RequestBody RequestVO<SysDictDataPageDTO> requestVO, HttpServletResponse response) throws IOException {
        sysDictDataService.excelExport(requestVO, response);
    }
}
