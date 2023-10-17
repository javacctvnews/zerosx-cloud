package com.zerosx.order.controller;

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
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;

import com.zerosx.order.vo.UserPageVO;
import com.zerosx.order.dto.UserPageDTO;
import com.zerosx.order.dto.UserDTO;
import com.zerosx.order.vo.UserVO;
import com.zerosx.order.service.IUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 加解密DEMO
 * @Description
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Slf4j
@RestController
@Tag(name = "加解密DEMO")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary ="分页列表")
    @OpLog(mod = "加解密DEMO", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/user/page_list")
    public ResultVO<CustomPageVO<UserPageVO>> pageList(@RequestBody RequestVO<UserPageDTO> requestVO){
        return ResultVOUtil.success(userService.pageList(requestVO, true));
    }

    @Operation(summary ="新增")
    @OpLog(mod = "加解密DEMO", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/user/save")
    public ResultVO<?> add(@Validated @RequestBody UserDTO userDTO) {
        return ResultVOUtil.successBoolean(userService.add(userDTO));
    }

    @Operation(summary ="编辑")
    @OpLog(mod = "加解密DEMO", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/user/update")
    public ResultVO<?> update(@Validated @RequestBody UserDTO userDTO) {
        return ResultVOUtil.successBoolean(userService.update(userDTO));
    }

    @Operation(summary ="按id查询")
    @GetMapping("/user/queryById/{id}")
    public ResultVO<UserVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(userService.queryById(id));
    }

    @Operation(summary ="删除")
    @OpLog(mod = "加解密DEMO", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/user/delete/{ids}")
    public ResultVO<?> deleteRecord(@PathVariable("ids") Long[] ids){
        return ResultVOUtil.successBoolean(userService.deleteRecord(ids));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "加解密DEMO", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/user/export")
    public void operatorExport(@RequestBody RequestVO<UserPageDTO> requestVO, HttpServletResponse response) throws IOException {
        userService.excelExport(requestVO, response);
    }

}
