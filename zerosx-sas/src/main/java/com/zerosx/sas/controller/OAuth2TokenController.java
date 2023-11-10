package com.zerosx.sas.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.sas.service.IOAuth2TokenService;
import com.zerosx.sas.vo.TokenQueryVO;
import com.zerosx.sas.vo.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Oauth2TokenController
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-10-30 09:50
 **/
@RestController
@Tag(name = "OAuth2Token管理")
@Slf4j
public class OAuth2TokenController {

    @Autowired
    private IOAuth2TokenService oAuth2TokenService;
    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @Operation(summary = "token分页列表")
    @OpLog(mod = "TOKEN管理", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/token2/page_list")
    public ResultVO<CustomPageVO<TokenVO>> pageList(@RequestBody @Validated RequestVO<TokenQueryVO> requestVO) {
        return ResultVOUtil.success(oAuth2TokenService.pageList(requestVO, true));
    }

    @Operation(summary = "退出登录")
    @OpLog(mod = "TOKEN管理", btn = "退出登录", opType = OpTypeEnum.QUERY)
    @PostMapping("/token2/logout")
    public ResultVO<?> logout(@RequestBody TokenQueryVO tokenQueryVO) {
        return ResultVOUtil.success(oAuth2TokenService.logout(tokenQueryVO));
    }

    /**
     * redis的set、list集合的过期时间是按照整个key来设置的，如果一直有人登录，则过期时间会重置，导致数据越来越大
     * 关联的key是TokenStoreConstants.CLIENT_ID_TO_ACCESS和TokenStoreConstants.UNAME_TO_ACCESS
     *
     * @return
     */
    @Operation(summary = "清理Token数据")
    @OpLog(mod = "TOKEN管理", btn = "清理Token数据", opType = OpTypeEnum.QUERY)
    @PostMapping("/token2/clean_token_data")
    public ResultVO<?> cleanTokenData(@RequestBody TokenQueryVO tokenQueryVO) {
        return ResultVOUtil.success(oAuth2TokenService.cleanTokenData(tokenQueryVO));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "TOKEN管理", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/token2/export")
    public void export(@RequestBody @Validated RequestVO<TokenQueryVO> requestVO, HttpServletResponse response) throws IOException {
        oAuth2TokenService.excelExport(requestVO,response);
    }

}
