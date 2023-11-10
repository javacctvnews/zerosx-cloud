package com.zerosx.sas.controller;

import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.sas.dto.Oauth2RegisteredClientDTO;
import com.zerosx.sas.dto.Oauth2RegisteredClientPageDTO;
import com.zerosx.sas.service.IOauth2RegisteredClientService;
import com.zerosx.sas.vo.Oauth2RegisteredClientPageVO;
import com.zerosx.sas.vo.Oauth2RegisteredClientVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName Oauth2RegisteredClientController
 * @Description 应用管理
 * @Author javacctvnews
 * @Date 2023/3/27 11:09
 * @Version 1.0
 */
@Tag(name = "客户端管理")
@RestController
@Slf4j
public class Oauth2RegisteredClientController {

    @Autowired
    private IOauth2RegisteredClientService oauth2RegisteredClientService;

    @Operation(summary = "分页查询")
    @OpLog(mod = "应用管理", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/oauth_client_details/list_page")
    public ResultVO<CustomPageVO<Oauth2RegisteredClientPageVO>> listPage(@RequestBody RequestVO<Oauth2RegisteredClientPageDTO> requestVO) {
        return ResultVOUtil.success(oauth2RegisteredClientService.listPage(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "应用管理", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/oauth_client_details/save")
    public ResultVO saveOauthClient(@Validated @RequestBody Oauth2RegisteredClientDTO oauth2RegisteredClientDTO) {
        return ResultVOUtil.successBoolean(oauth2RegisteredClientService.saveOauthClient(oauth2RegisteredClientDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "应用管理", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/oauth_client_details/edit")
    public ResultVO editOauthClient(@Validated @RequestBody Oauth2RegisteredClientDTO oauth2RegisteredClientDTO) {
        return ResultVOUtil.successBoolean(oauth2RegisteredClientService.saveOauthClient(oauth2RegisteredClientDTO));
    }

    @Operation(summary = "修改密码")
    @OpLog(mod = "应用管理", btn = "修改密码", opType = OpTypeEnum.UPDATE)
    @PostMapping("/oauth_client_details/edit_pwd")
    public ResultVO editOauthClientPwd(@Validated @RequestBody Oauth2RegisteredClientDTO oauth2RegisteredClientDTO) {
        return ResultVOUtil.successBoolean(oauth2RegisteredClientService.editOauthClientPwd(oauth2RegisteredClientDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/oauth_client_details/queryById/{id}")
    public ResultVO<Oauth2RegisteredClientVO> queryById(@PathVariable String id) {
        return ResultVOUtil.success(oauth2RegisteredClientService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "客户端管理", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/oauth_client_details/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") String[] ids) {
        return ResultVOUtil.successBoolean(oauth2RegisteredClientService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "客户端管理", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/oauth_client_details/export")
    public void operatorExport(@RequestBody RequestVO<Oauth2RegisteredClientPageDTO> requestVO, HttpServletResponse response) {
        oauth2RegisteredClientService.excelExport(requestVO, response);
    }

    @Operation(summary = "下拉框")
    @PostMapping("/oauth_client_details/select")
    public ResultVO<List<SelectOptionVO>> selectList() {
        return ResultVOUtil.success(oauth2RegisteredClientService.selectList());
    }

    @Operation(summary = "按clientId查询")
    @GetMapping("/oauth_client_details/{clientId}")
    public ResultVO<OauthClientDetailsBO> getClient(@PathVariable("clientId") String clientId) {
        return ResultVOUtil.success(oauth2RegisteredClientService.getClient(clientId));
    }

}
