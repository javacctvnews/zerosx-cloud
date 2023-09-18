package com.zerosx.system.controller;

import com.zerosx.common.core.vo.CustomPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.BeanCopierUtil;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;

import com.zerosx.system.vo.SmsSupplierBusinessPageVO;
import com.zerosx.system.dto.SmsSupplierBusinessPageDTO;
import com.zerosx.system.dto.SmsSupplierBusinessDTO;
import com.zerosx.system.vo.SmsSupplierBusinessVO;
import com.zerosx.system.entity.SmsSupplierBusiness;
import com.zerosx.system.service.ISmsSupplierBusinessService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 短信业务模板
 * @Description
 * @author javacctvnews
 * @date 2023-08-31 10:39:49
 */
@Slf4j
@RestController
@Tag(name = "短信业务模板")
public class SmsSupplierBusinessController {

    @Autowired
    private ISmsSupplierBusinessService smsSupplierBusinessService;

    @Operation(summary ="分页列表")
    @SystemLog(title = "短信业务模板", btnName = "分页查询", businessType= BusinessType.QUERY)
    @PostMapping("/sms_supplier_business/page_list")
    public ResultVO<CustomPageVO<SmsSupplierBusinessPageVO>> pageList(@RequestBody RequestVO<SmsSupplierBusinessPageDTO> requestVO){
        return ResultVOUtil.success(smsSupplierBusinessService.pageList(requestVO, true));
    }

    @Operation(summary ="新增")
    @SystemLog(title = "短信业务模板", btnName = "新增", businessType= BusinessType.INSERT)
    @PostMapping("/sms_supplier_business/save")
    public ResultVO<?> add(@Validated @RequestBody SmsSupplierBusinessDTO smsSupplierBusinessDTO) {
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.add(smsSupplierBusinessDTO));
    }

    @Operation(summary ="编辑")
    @SystemLog(title = "短信业务模板", btnName = "编辑", businessType= BusinessType.UPDATE)
    @PostMapping("/sms_supplier_business/update")
    public ResultVO<?> update(@Validated @RequestBody SmsSupplierBusinessDTO smsSupplierBusinessDTO) {
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.update(smsSupplierBusinessDTO));
    }

    @Operation(summary ="按id查询")
    @GetMapping("/sms_supplier_business/queryById/{id}")
    public ResultVO<SmsSupplierBusinessVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(smsSupplierBusinessService.queryById(id));
    }

    @Operation(summary ="删除")
    @SystemLog(title = "短信业务模板", btnName = "删除", businessType= BusinessType.DELETE)
    @DeleteMapping("/sms_supplier_business/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids){
        return ResultVOUtil.successBoolean(smsSupplierBusinessService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "短信业务模板", btnName = "导出", businessType= BusinessType.EXPORT)
    @PostMapping("/sms_supplier_business/export")
    public void operatorExport(@RequestBody RequestVO<SmsSupplierBusinessPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SmsSupplierBusinessPageVO> pages = smsSupplierBusinessService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), "短信业务模板", SmsSupplierBusinessPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

}
