package com.zerosx.system.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.system.dto.OssSupplierDTO;
import com.zerosx.system.dto.OssSupplierPageDTO;
import com.zerosx.system.service.IOssSupplierService;
import com.zerosx.system.vo.OssSupplierPageVO;
import com.zerosx.system.vo.OssSupplierVO;
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
 * @Description
 * @author javacctvnews
 * @date 2023-09-08 18:23:18
 */
@Slf4j
@RestController
@Tag(name = "OSS配置")
public class OssSupplierController {

    @Autowired
    private IOssSupplierService ossSupplierService;

    @Operation(summary ="分页列表")
    @SystemLog(title = "OSS配置", btnName = "分页查询", businessType= BusinessType.QUERY)
    @PostMapping("/oss_supplier/page_list")
    public ResultVO<CustomPageVO<OssSupplierPageVO>> pageList(@RequestBody RequestVO<OssSupplierPageDTO> requestVO){
        return ResultVOUtil.success(ossSupplierService.pageList(requestVO, true));
    }

    @Operation(summary ="新增")
    @SystemLog(title = "OSS配置", btnName = "新增", businessType= BusinessType.INSERT)
    @PostMapping("/oss_supplier/save")
    public ResultVO<?> add(@Validated @RequestBody OssSupplierDTO ossSupplierDTO) {
        return ResultVOUtil.successBoolean(ossSupplierService.add(ossSupplierDTO));
    }

    @Operation(summary ="编辑")
    @SystemLog(title = "OSS配置", btnName = "编辑", businessType= BusinessType.UPDATE)
    @PostMapping("/oss_supplier/update")
    public ResultVO<?> update(@Validated @RequestBody OssSupplierDTO ossSupplierDTO) {
        return ResultVOUtil.successBoolean(ossSupplierService.update(ossSupplierDTO));
    }

    @Operation(summary ="按id查询")
    @SystemLog(title = "OSS配置", btnName = "按id查询", businessType= BusinessType.QUERY)
    @GetMapping("/oss_supplier/queryById/{id}")
    public ResultVO<OssSupplierVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(ossSupplierService.queryById(id));
    }

    @Operation(summary ="删除")
    @SystemLog(title = "OSS配置", btnName = "删除", businessType= BusinessType.DELETE)
    @DeleteMapping("/oss_supplier/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids){
        return ResultVOUtil.successBoolean(ossSupplierService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "OSS配置", btnName = "导出", businessType= BusinessType.EXPORT)
    @PostMapping("/oss_supplier/export")
    public void operatorExport(@RequestBody RequestVO<OssSupplierPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<OssSupplierPageVO> pages = ossSupplierService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), OssSupplierPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

}
