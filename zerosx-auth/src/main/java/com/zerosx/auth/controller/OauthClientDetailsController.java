package com.zerosx.auth.controller;

import com.zerosx.auth.dto.OauthClientDetailsDTO;
import com.zerosx.auth.dto.OauthClientDetailsPageDTO;
import com.zerosx.auth.service.IOauthClientDetailsService;
import com.zerosx.auth.vo.OauthClientDetailsPageVO;
import com.zerosx.auth.vo.OauthClientDetailsVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName OauthClientDetailsController
 * @Description 应用管理
 * @Author javacctvnews
 * @Date 2023/3/27 11:09
 * @Version 1.0
 */
@Tag(name = "客户端管理")
@RestController
@Slf4j
public class OauthClientDetailsController {

    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;


    @Operation(summary = "分页查询")
    @SystemLog(title = "应用管理", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/oauth_client_details/list_page")
    public ResultVO<CustomPageVO<OauthClientDetailsPageVO>> listPage(@RequestBody RequestVO<OauthClientDetailsPageDTO> requestVO) {
        return ResultVOUtil.success(oauthClientDetailsService.listPage(requestVO, true));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "应用管理", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/oauth_client_details/save")
    public ResultVO saveOauthClient(@Validated @RequestBody OauthClientDetailsDTO oauthClientDetailsDTO) {
        return ResultVOUtil.successBoolean(oauthClientDetailsService.saveOauthClient(oauthClientDetailsDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "应用管理", btnName = "编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/oauth_client_details/edit")
    public ResultVO editOauthClient(@Validated @RequestBody OauthClientDetailsDTO oauthClientDetailsEditDTO) {
        return ResultVOUtil.successBoolean(oauthClientDetailsService.editOauthClient(oauthClientDetailsEditDTO));
    }

    @Operation(summary = "按id查询")
    @SystemLog(title = "客户端管理", btnName = "按id查询", businessType = BusinessType.QUERY)
    @GetMapping("/oauth_client_details/queryById/{id}")
    public ResultVO<OauthClientDetailsVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(oauthClientDetailsService.queryById(id));
    }

    @Operation(summary = "删除")
    @SystemLog(title = "客户端管理", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/oauth_client_details/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids) {
        return ResultVOUtil.successBoolean(oauthClientDetailsService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "客户端管理", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/oauth_client_details/export")
    public void operatorExport(@RequestBody RequestVO<OauthClientDetailsPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<OauthClientDetailsPageVO> pages = oauthClientDetailsService.listPage(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), OauthClientDetailsPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/oauth_client_details/select")
    public ResultVO<List<SelectOptionVO>> selectList() {
        return ResultVOUtil.success(oauthClientDetailsService.selectList());
    }

}
