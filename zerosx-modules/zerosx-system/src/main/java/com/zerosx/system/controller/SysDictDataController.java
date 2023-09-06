package com.zerosx.system.controller;


import com.zerosx.api.system.ISysDictDataApi;
import com.zerosx.api.system.dto.SysDictDataRetrieveDTO;
import com.zerosx.api.system.vo.I18nSelectOptionVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SysDictDataDTO;
import com.zerosx.system.dto.SysDictDataUpdateDTO;
import com.zerosx.system.service.ISysDictDataService;
import com.zerosx.system.vo.SysDictDataVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 字典数据表 前端控制器
 */
@Tag(name = "字典数据")
@RestController
@Slf4j
public class SysDictDataController implements ISysDictDataApi {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Operation(summary = "新增")
    @PostMapping(value = "/sysDictData_insert")
    @SystemLog(title = "字典数据", btnName = "新增", businessType = BusinessType.INSERT)
    public ResultVO<?> insertSysDictData(@RequestBody @Validated SysDictDataDTO sysDictDataDTO) {
        return ResultVOUtil.successBoolean(sysDictDataService.insert(sysDictDataDTO));
    }

    @Operation(summary = "编辑")
    @PutMapping(value = "/sysDictData_update")
    @SystemLog(title = "字典数据-编辑", btnName = "编辑", businessType = BusinessType.UPDATE)
    public ResultVO<?> updateSysDictData(@RequestBody @Validated SysDictDataUpdateDTO sysDictDataUpdateDTO) {
        return ResultVOUtil.successBoolean(sysDictDataService.updateSysDictData(sysDictDataUpdateDTO));
    }

    @Operation(summary = "分页查询")
    @PostMapping(value = "/sysDictData_page")
    @SystemLog(title = "字典数据", btnName = "分页查询", businessType = BusinessType.QUERY)
    public ResultVO<CustomPageVO<SysDictDataVO>> selectBySysDictDataPage(@RequestBody RequestVO<SysDictDataRetrieveDTO> sysDictData) {
        return ResultVOUtil.success(sysDictDataService.pageList(sysDictData, true));
    }

    @Operation(summary = "查询指定字典类型的数据集合")
    @GetMapping(value = "/sysDictData_selectList/{dictType}")
    @Override
    public ResultVO<List<I18nSelectOptionVO>> getSysDictDataSelectList(@PathVariable("dictType") String dictType) {
        return ResultVOUtil.success(sysDictDataService.getSysDictDataSelectList(dictType));
    }

    @Operation(summary = "批量查询字典类型的数据")
    @PostMapping(value = "/sysDictData_getMap")
    @Override
    public ResultVO<Map<String, Map<Object, Object>>> getSysDictDataMap(@RequestBody List<String> dictType) {
        return ResultVOUtil.success(sysDictDataService.getSysDictDataGetMap(dictType));
    }

    @Operation(summary = "根据字典类型查询字典数据")
    @PostMapping(value = "/sysDictData_list")
    public ResultVO<List<SysDictDataVO>> getSysDictDataList(@RequestBody SysDictDataRetrieveDTO sysDictData) {
        return ResultVOUtil.success(sysDictDataService.selectDictDataList(sysDictData));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "字典数据", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/sysDictData_delete/{dictCode}")
    public ResultVO<?> deleteSysDictData(@PathVariable("dictCode") Long[] dictCode) {
        return ResultVOUtil.successBoolean(sysDictDataService.deleteSysDictData(dictCode));
    }

    @Operation(summary = "查询字典类型的字典数据")
    @GetMapping(value = "/sysDictData/{dictType}/type")
    public ResultVO<Map<String, String>> getDictData(@PathVariable("dictType") String dictType) {
        return ResultVOUtil.success(sysDictDataService.getDictDataByDictType(dictType));
    }

    @Operation(summary = "刷新缓存")
    @SystemLog(title = "字典数据", btnName = "刷新缓存", businessType = BusinessType.UPDATE)
    @GetMapping(value = "/sysDictData/init")
    public ResultVO<?> initDictData(@RequestParam(value = "dictType", required = false) String dictType) {
        sysDictDataService.initCacheDictData(dictType);
        return ResultVOUtil.success();
    }

    @Operation(summary = "按id查询字典详情")
    @GetMapping(value = "/getDictById/{id}")
    public ResultVO<SysDictDataVO> getDictById(@PathVariable("id") Long id) {
        return ResultVOUtil.success(sysDictDataService.getDictById(id));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "字典数据", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sysDictData/export")
    public void operatorExport(@RequestBody RequestVO<SysDictDataRetrieveDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysDictDataVO> pages = sysDictDataService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), SysDictDataVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }
}
