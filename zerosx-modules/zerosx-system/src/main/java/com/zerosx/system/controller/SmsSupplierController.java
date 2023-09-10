package com.zerosx.system.controller;

import com.zerosx.api.system.ISmsSupplierControllerApi;
import com.zerosx.api.system.dto.SmsSendDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.SmsSupplierDTO;
import com.zerosx.system.dto.SmsSupplierPageDTO;
import com.zerosx.system.service.ISmsSupplierService;
import com.zerosx.system.vo.SmsSupplierPageVO;
import com.zerosx.system.vo.SmsSupplierVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信配置
 *
 * @author javacctvnews
 * @Description
 * @date 2023-08-30 18:28:13
 */
@Slf4j
@RestController
@Tag(name = "短信配置")
public class SmsSupplierController implements ISmsSupplierControllerApi {

    @Autowired
    private ISmsSupplierService smsSupplierService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "短信配置", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/sms_supplier/page_list")
    public ResultVO<CustomPageVO<SmsSupplierPageVO>> pageList(@RequestBody RequestVO<SmsSupplierPageDTO> requestVO) {
        return ResultVOUtil.success(smsSupplierService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "短信配置", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/sms_supplier/save")
    public ResultVO<?> add(@Validated @RequestBody SmsSupplierDTO smsSupplierDTO) {
        return ResultVOUtil.successBoolean(smsSupplierService.add(smsSupplierDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "短信配置", btnName = "编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/sms_supplier/update")
    public ResultVO<?> update(@Validated @RequestBody SmsSupplierDTO smsSupplierDTO) {
        return ResultVOUtil.successBoolean(smsSupplierService.update(smsSupplierDTO));
    }

    @Operation(summary = "按id查询")
    @SystemLog(title = "短信配置", btnName = "按id查询", businessType = BusinessType.QUERY)
    @GetMapping("/sms_supplier/queryById/{id}")
    public ResultVO<SmsSupplierVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(smsSupplierService.queryById(id));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "短信配置", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/sms_supplier/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(smsSupplierService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "短信配置", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sms_supplier/export")
    public void operatorExport(@RequestBody RequestVO<SmsSupplierPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SmsSupplierPageVO> pages = smsSupplierService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), "短信配置", SmsSupplierPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "发送短信")
    @SystemLog(title = "短信配置", btnName = "发送短信", businessType = BusinessType.INSERT)
    @PostMapping("/sms/send")
    public ResultVO<?> sendSms(@Validated @RequestBody SmsSendDTO smsSendDTO) {
        return smsSupplierService.sendSms(smsSendDTO);
    }

}
