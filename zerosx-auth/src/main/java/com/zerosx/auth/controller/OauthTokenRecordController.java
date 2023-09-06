package com.zerosx.auth.controller;

import com.zerosx.auth.dto.OauthTokenRecordPageDTO;
import com.zerosx.auth.service.IOauthTokenRecordService;
import com.zerosx.auth.vo.OauthTokenRecordPageVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录日志
 *
 * @author javacctvnews
 * @Description
 * @date 2023-04-09 15:33:32
 */
@Slf4j
@RestController
@Tag(name = "登录日志")
public class OauthTokenRecordController {

    @Autowired
    private IOauthTokenRecordService oauthTokenRecordService;

    @Operation(summary = "分页列表")
    @SystemLog(title = "登录日志", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/oauth_token_record/page_list")
    public ResultVO<CustomPageVO<OauthTokenRecordPageVO>> pageList(@RequestBody RequestVO<OauthTokenRecordPageDTO> requestVO) {
        return ResultVOUtil.success(oauthTokenRecordService.pageList(requestVO, true));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "登录日志", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/oauth_token_record/delete/{id}")
    public ResultVO<?> deleteRecord(@PathVariable("id") Long[] id) {
        return ResultVOUtil.successBoolean(oauthTokenRecordService.deleteRecord(id));
    }

    @Operation(summary = "清空所有")
    @SystemLog(title = "登录日志", btnName = "清空所有", businessType = BusinessType.DELETE)
    @DeleteMapping("/oauth_token_record/delete_all")
    public ResultVO<?> deleteAll() {
        return ResultVOUtil.successBoolean(oauthTokenRecordService.deleteAll());
    }

    @Operation(summary = "导出")
    @SystemLog(title = "登录日志", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/oauth_token_record/export")
    public void operatorExport(@RequestBody RequestVO<OauthTokenRecordPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<OauthTokenRecordPageVO> pageInfo = oauthTokenRecordService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pageInfo.getList(), OauthTokenRecordPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pageInfo.getTotal(), System.currentTimeMillis() - t1);
    }

}
