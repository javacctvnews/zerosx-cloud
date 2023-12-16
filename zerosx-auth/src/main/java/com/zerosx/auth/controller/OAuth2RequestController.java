package com.zerosx.auth.controller;

import com.zerosx.auth.service.IOauthClientDetailsService;
import com.zerosx.auth.vo.TokenQueryVO;
import com.zerosx.auth.vo.TokenVO;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Tag(name = "TOKEN管理")
@RestController
public class OAuth2RequestController {

    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @Operation(summary = "token分页列表")
    @OpLog(mod = "TOKEN管理", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/token/page_list")
    public ResultVO<CustomPageVO<TokenVO>> pageList(@RequestBody @Validated RequestVO<TokenQueryVO> requestVO) {
        return ResultVOUtil.success(oauthClientDetailsService.pageList(requestVO, true));
    }

    @Operation(summary = "退出登录")
    @OpLog(mod = "TOKEN管理", btn = "退出登录", opType = OpTypeEnum.QUERY)
    @PostMapping("/oauth/token/logout")
    public ResultVO<?> logout(@RequestBody TokenQueryVO tokenQueryVO) {
        return ResultVOUtil.success(oauthClientDetailsService.logout(tokenQueryVO));
    }

    /**
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     * 关联的key是TokenStoreConstants.CLIENT_ID_TO_ACCESS和TokenStoreConstants.UNAME_TO_ACCESS
     *
     * @return
     */
    @Operation(summary = "清理Token数据")
    @OpLog(mod = "TOKEN管理", btn = "清理Token数据", opType = OpTypeEnum.QUERY)
    @PostMapping("/token/clean_token_data")
    public ResultVO<?> cleanTokenData(@RequestBody TokenQueryVO tokenQueryVO) {
        return ResultVOUtil.success(oauthClientDetailsService.cleanTokenData(tokenQueryVO));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "TOKEN管理", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/token/export")
    public void operatorExport(@RequestBody @Validated RequestVO<TokenQueryVO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<TokenVO> pageInfo = oauthClientDetailsService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pageInfo.getList(), TokenVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pageInfo.getTotal(), System.currentTimeMillis() - t1);
    }

}
