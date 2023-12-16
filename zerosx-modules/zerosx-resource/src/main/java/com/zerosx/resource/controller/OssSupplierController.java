package com.zerosx.resource.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.OssSupplierDTO;
import com.zerosx.resource.dto.OssSupplierPageDTO;
import com.zerosx.resource.service.IOssSupplierService;
import com.zerosx.resource.vo.OssSupplierPageVO;
import com.zerosx.resource.vo.OssSupplierVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OSS配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-09-08 18:23:18
 */
@Slf4j
@RestController
@Tag(name = "OSS配置")
public class OssSupplierController {

    @Autowired
    private IOssSupplierService ossSupplierService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "OSS配置", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/oss_supplier/page_list")
    public ResultVO<CustomPageVO<OssSupplierPageVO>> pageList(@RequestBody RequestVO<OssSupplierPageDTO> requestVO) {
        return ResultVOUtil.success(ossSupplierService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "OSS配置", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/oss_supplier/save")
    public ResultVO<?> add(@Validated @RequestBody OssSupplierDTO ossSupplierDTO) {
        return ResultVOUtil.successBoolean(ossSupplierService.add(ossSupplierDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "OSS配置", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/oss_supplier/update")
    public ResultVO<?> update(@Validated @RequestBody OssSupplierDTO ossSupplierDTO) {
        return ResultVOUtil.successBoolean(ossSupplierService.update(ossSupplierDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/oss_supplier/queryById/{id}")
    public ResultVO<OssSupplierVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(ossSupplierService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "OSS配置", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/oss_supplier/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(ossSupplierService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "OSS配置", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/oss_supplier/export")
    public void operatorExport(@RequestBody RequestVO<OssSupplierPageDTO> requestVO, HttpServletResponse response) throws IOException {
        ossSupplierService.excelExport(requestVO, response);
    }

}
