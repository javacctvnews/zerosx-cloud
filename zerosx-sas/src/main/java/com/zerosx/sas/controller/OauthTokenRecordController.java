package com.zerosx.sas.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.sas.dto.OauthTokenRecordPageDTO;
import com.zerosx.sas.service.IOauthTokenRecordService;
import com.zerosx.sas.vo.OauthTokenRecordPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @OpLog(mod = "登录日志", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/oauth_token_record/page_list")
    public ResultVO<CustomPageVO<OauthTokenRecordPageVO>> pageList(@RequestBody RequestVO<OauthTokenRecordPageDTO> requestVO) {
        return ResultVOUtil.success(oauthTokenRecordService.pageList(requestVO, true));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "登录日志", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/oauth_token_record/delete/{id}")
    public ResultVO<?> deleteRecord(@PathVariable("id") Long[] id) {
        return ResultVOUtil.successBoolean(oauthTokenRecordService.deleteRecord(id));
    }

    @Operation(summary = "清空所有")
    @OpLog(mod = "登录日志", btn = "清空所有", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/oauth_token_record/delete_all")
    public ResultVO<?> deleteAll() {
        return ResultVOUtil.successBoolean(oauthTokenRecordService.deleteAll());
    }

    @Operation(summary = "导出")
    @OpLog(mod = "登录日志", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/oauth_token_record/export")
    public void operatorExport(@RequestBody RequestVO<OauthTokenRecordPageDTO> requestVO, HttpServletResponse response) throws IOException {
        oauthTokenRecordService.excelExport(requestVO, response);
    }

}
