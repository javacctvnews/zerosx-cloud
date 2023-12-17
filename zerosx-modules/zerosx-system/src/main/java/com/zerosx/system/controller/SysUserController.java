package com.zerosx.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.auth.IAccessTokenClient;
import com.zerosx.api.system.ISysUserClient;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.api.system.vo.LoginUserVO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.BizTagEnum;
import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.LeafUtils;
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
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAccessTokenClient authFeignServiceApi;

    @Operation(summary = "分页列表")
    @OpLog(mod = "系统用户", btn = "分页查询", opType = OpTypeEnum.QUERY)
    @PostMapping("/sys_user/page_list")
    public ResultVO<CustomPageVO<SysUserPageVO>> pageList(@RequestBody RequestVO<SysUserPageDTO> requestVO) {
        return ResultVOUtil.success(sysUserService.pageList(requestVO, true));
    }

    @Operation(summary = "分页列表2-测试")
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

    @Operation(summary = "新增测试用户")
    @GetMapping("/sys_user/add_test_users/{num}/{prefix}")
    public ResultVO<?> test001(@PathVariable("num") Integer num, @PathVariable("prefix") String prefix) throws Exception {
        List<SysUser> srs = new ArrayList<>();
        String admin123 = passwordEncoder.encode("Admin123");
        for (Integer i = 1; i <= num; i++) {
            //long t1 = System.currentTimeMillis();
            SysUser sysUser = new SysUser();
            sysUser.setId(LeafUtils.uid(BizTagEnum.USER_CODE));
            sysUser.setUserCode(sysUser.getId().toString());
            sysUser.setPassword(admin123);
            sysUser.setUserName(prefix + i);
            sysUser.setNickName("测试账号" + i);
            sysUser.setEmail("12324" + String.format("%08d", i) + "@qq.com");
            sysUser.setPhoneNumber("188" + String.format("%08d", i));
            sysUser.setUserType(UserTypeEnum.TENANT_OPERATOR.getCode());
            sysUser.setDeptId(1L);
            sysUser.setSex("2");
            sysUser.setOperatorId("433980");
            sysUser.setRemark("测试账号" + i);
            //sysUserService.save(sysUser);
            //log.debug("创建测试用户 耗时{}ms", System.currentTimeMillis() - t1);
            srs.add(sysUser);
        }
        List<List<SysUser>> partitions = ListUtils.partition(srs, 1000);
        for (List<SysUser> users : partitions) {
            sysUserService.saveBatch(users);
            Thread.sleep(1000);
        }
        return ResultVOUtil.success();
    }

    @Operation(summary = "批量登录")
    @GetMapping("/sys_user/login/{num}")
    public ResultVO<?> login(@PathVariable("num") Integer num) throws InterruptedException {
        for (Integer i = 1; i <= num; i++) {
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("client_id", "saas");
            multiValueMap.add("client_secret", "Zeros9999!#@");
            multiValueMap.add("grant_type", "password");
            multiValueMap.add("username", "zeros" + i);
            multiValueMap.add("password", "Admin123");
            multiValueMap.add("user_auth_type", "SysUser");
            ResultVO resultVO = authFeignServiceApi.postAccessToken(multiValueMap);
            System.out.println("resultVO = " + resultVO);
            Thread.sleep(1 * 1000);
        }
        return ResultVOUtil.success();
    }

}
