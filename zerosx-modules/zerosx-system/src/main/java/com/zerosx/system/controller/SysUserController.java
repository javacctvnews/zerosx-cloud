package com.zerosx.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.system.ISysUserClient;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.log.anno.OpLog;
import com.zerosx.common.log.enums.OpTypeEnum;
import com.zerosx.system.dto.SysUserDTO;
import com.zerosx.system.dto.SysUserPageDTO;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.service.ISysUserService;
import com.zerosx.system.vo.SysUserPageVO;
import com.zerosx.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 系统用户
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-20 13:48:04
 */
@Slf4j
@RestController
@Tag(name = "系统用户")
public class SysUserController implements ISysUserClient {

    @Autowired
    private ISysUserService sysUserService;

    @Operation(summary = "分页列表")
    @OpLog(mod = "系统用户", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_user/page_list")
    public ResultVO<CustomPageVO<SysUserPageVO>> pageList(@RequestBody RequestVO<SysUserPageDTO> requestVO) {
        return ResultVOUtil.success(sysUserService.pageList(requestVO, true));
    }

    @Operation(summary = "分页列表-测试")
    @OpLog(mod = "系统用户", btn = "分页查询2", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_user/page_list2")
    public ResultVO<CustomPageVO<SysUser>> pageList2(@RequestBody RequestVO<SysUserPageDTO> requestVO) {
        return ResultVOUtil.success(sysUserService.pageList2(requestVO, true));
    }

    @Operation(summary = "新增")
    @OpLog(mod = "系统用户", btn = "新增", opType = OpTypeEnum.INSERT)
    @PostMapping("/sys_user/save")
    public ResultVO<?> add(@Validated @RequestBody SysUserDTO sysUserDTO) {
        return ResultVOUtil.successBoolean(sysUserService.add(sysUserDTO));
    }

    @Operation(summary = "编辑")
    @OpLog(mod = "系统用户", btn = "编辑", opType = OpTypeEnum.UPDATE)
    @PostMapping("/sys_user/update")
    public ResultVO<?> update(@Validated @RequestBody SysUserDTO sysUserDTO) {
        return ResultVOUtil.successBoolean(sysUserService.update(sysUserDTO));
    }

    @Operation(summary = "按id查询")
    @GetMapping("/sys_user/queryById/{id}")
    public ResultVO<SysUserVO> queryById(@PathVariable Long id) {
        return ResultVOUtil.success(sysUserService.queryById(id));
    }

    @Operation(summary = "删除")
    @OpLog(mod = "系统用户", btn = "删除", opType = OpTypeEnum.DELETE)
    @DeleteMapping("/sys_user/delete/{userId}")
    public ResultVO<?> deleteRecord(@PathVariable("userId") Long[] userId) {
        return ResultVOUtil.successBoolean(sysUserService.deleteRecord(userId));
    }

    @Operation(summary = "导出")
    @OpLog(mod = "系统用户", btn = "导出", opType = OpTypeEnum.EXPORT)
    @PostMapping("/sys_user/export")
    public void operatorExport(@RequestBody RequestVO<SysUserPageDTO> requestVO, HttpServletResponse response) throws IOException {
        sysUserService.excelExport(requestVO, response);
    }

    @Operation(summary = "查询登录用户信息")
    @PostMapping("/sys_user/query_login_user")
    public ResultVO<LoginUserVO> queryLoginUser(@RequestBody UserLoginDTO userLoginDTO) {
        return ResultVOUtil.success(sysUserService.queryLoginUser(userLoginDTO));
    }

    @Operation(summary = "登录用户信息")
    @GetMapping("/sys_user/getInfo")
    public ResultVO<Map<String, Object>> getInfo() {
        return ResultVOUtil.success(sysUserService.getCurrentUserInfo());
    }

    @Operation(summary = "个人信息")
    @GetMapping("/sys_user/profile")
    public ResultVO<Map<String, Object>> getProfile() {
        return ResultVOUtil.success(sysUserService.getUserProfile());
    }

    @Operation(summary = "更换用户头像")
    @PostMapping("/sys_user/update_avatar")
    public ResultVO<?> getProfile(@RequestBody SysUserDTO sysUserDTO) {
        if (StringUtils.isBlank(sysUserDTO.getAvatar())) {
            throw new BusinessException("头像存储标识不能为空");
        }
        LambdaUpdateWrapper<SysUser> upqw = Wrappers.lambdaUpdate(SysUser.class);
        upqw.eq(SysUser::getUserName, ZerosSecurityContextHolder.getUserName());
        upqw.set(SysUser::getAvatar, sysUserDTO.getAvatar());
        return ResultVOUtil.successBoolean(sysUserService.update(upqw));
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/sys_user/update_profile")
    public ResultVO<?> updateProfile(@RequestBody SysUserDTO sysUserDTO) {
        return ResultVOUtil.successBoolean(sysUserService.updateProfile(sysUserDTO));
    }

    /**
     * 查询用户的租户ID信息
     *
     * @param userName
     * @return
     */
    @Operation(summary = "按用户名查询当前用户信息")
    @PostMapping("/sys_user/current_login_user")
    public ResultVO<LoginUserTenantsBO> currentLoginUser(@RequestParam("userName") String userName) {
        return ResultVOUtil.success(sysUserService.currentLoginUser(userName));
    }

}
