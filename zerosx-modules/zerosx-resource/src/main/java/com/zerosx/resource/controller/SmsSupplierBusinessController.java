package com.zerosx.resource.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.SmsSupplierBusinessDTO;
import com.zerosx.resource.dto.SmsSupplierBusinessPageDTO;
import com.zerosx.resource.service.ISmsSupplierBusinessService;
import com.zerosx.resource.vo.SmsSupplierBusinessPageVO;
import com.zerosx.resource.vo.SmsSupplierBusinessVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信业务模板
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-31 10:39:49
 */
@Slf4j
@RestController
@Tag(name = "短信业务模板")
public class SmsSupplierBusinessController {

    @Autowired
    private ISmsSupplierBusinessService smsSupplierBusinessService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "短信业务模板", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sms_supplier_business/page_list")
    public ResultVO<CustomPageVO<SmsSupplierBusinessPageVO>> pageList(@RequestBody RequestVO<SmsSupplierBusinessPageDTO> requestVO) {
        return ResultVOUtil.success(smsSupplierBusinessService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "短信业务模板", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sms_supplier_business/save")
    public ResultVO<?> add(@Validated @RequestBody SmsSupplierBusinessDTO smsSupplierBusinessDTO) {
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.add(smsSupplierBusinessDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "短信业务模板", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sms_supplier_business/update")
    public ResultVO<?> update(@Validated @RequestBody SmsSupplierBusinessDTO smsSupplierBusinessDTO) {
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.update(smsSupplierBusinessDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sms_supplier_business/queryById/{id}")
    public ResultVO<SmsSupplierBusinessVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(smsSupplierBusinessService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "短信业务模板", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sms_supplier_business/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "短信业务模板", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sms_supplier_business/export")
    public void operatorExport(@RequestBody RequestVO<SmsSupplierBusinessPageDTO> requestVO, HttpServletResponse response) throws IOException {
        smsSupplierBusinessService.excelExport(requestVO, response);
    }

}
