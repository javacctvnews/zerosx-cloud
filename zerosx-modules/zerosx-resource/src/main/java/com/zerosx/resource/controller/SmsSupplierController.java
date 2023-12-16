package com.zerosx.resource.controller;

import com.zerosx.api.resource.ISmsSupplierClient;
import com.zerosx.api.resource.dto.SmsSendDTO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.resource.dto.SmsCodeDTO;
import com.zerosx.resource.dto.SmsSupplierDTO;
import com.zerosx.resource.dto.SmsSupplierPageDTO;
import com.zerosx.resource.service.ISmsSupplierService;
import com.zerosx.resource.vo.SmsCodeVO;
import com.zerosx.resource.vo.SmsSupplierPageVO;
import com.zerosx.resource.vo.SmsSupplierVO;
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
public class SmsSupplierController implements ISmsSupplierClient {

    @Autowired
    private ISmsSupplierService smsSupplierService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "短信配置", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sms_supplier/page_list")
    public ResultVO<CustomPageVO<SmsSupplierPageVO>> pageList(@RequestBody RequestVO<SmsSupplierPageDTO> requestVO) {
        return ResultVOUtil.success(smsSupplierService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "短信配置", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sms_supplier/save")
    public ResultVO<?> add(@Validated @RequestBody SmsSupplierDTO smsSupplierDTO) {
        return ResultVOUtil.successBoolean(smsSupplierService.add(smsSupplierDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "短信配置", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sms_supplier/update")
    public ResultVO<?> update(@Validated @RequestBody SmsSupplierDTO smsSupplierDTO) {
        return ResultVOUtil.successBoolean(smsSupplierService.update(smsSupplierDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sms_supplier/queryById/{id}")
    public ResultVO<SmsSupplierVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(smsSupplierService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "短信配置", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sms_supplier/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(smsSupplierService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "短信配置", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sms_supplier/export")
    public void operatorExport(@RequestBody RequestVO<SmsSupplierPageDTO> requestVO, HttpServletResponse response) throws IOException {
        smsSupplierService.excelExport(requestVO, response);
    }

    @Operation(summary = "发送短信")
    @OpLog(mod = "短信配置", btn = "发送短信", opType = OpTypeEnum.INSERT)
    @PostMapping("/sms/send")
    public ResultVO<?> sendSms(@Validated @RequestBody SmsSendDTO smsSendDTO) {
        return smsSupplierService.sendSms(smsSendDTO);
    }

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @Operation(summary = "短信验证码")
    @PostMapping("/sms/getSmsCode")
    @OpLog(mod = "验证码", btn = "短信验证码", opType = OpTypeEnum.INSERT)
    public ResultVO<SmsCodeVO> getSmsCode(@RequestBody SmsCodeDTO smsCodeDTO) {
        return ResultVOUtil.success(smsSupplierService.getSmsCode(smsCodeDTO));
    }

}
