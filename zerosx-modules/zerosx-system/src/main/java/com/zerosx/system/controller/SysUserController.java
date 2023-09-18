package com.zerosx.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zerosx.api.auth.IAuthFeignServiceApi;
import com.zerosx.api.system.dto.UserLoginDTO;
import com.zerosx.common.base.exception.BusinessException;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.LoginUserTenantsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.easyexcel.EasyExcelUtil;
import com.zerosx.common.core.enums.UserTypeEnum;
import com.zerosx.common.core.interceptor.ZerosSecurityContextHolder;
import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.common.core.vo.CustomUserDetails;
import com.zerosx.common.log.annotation.SystemLog;
import com.zerosx.common.log.enums.BusinessType;
import com.zerosx.common.oss.model.OssObjectVO;
import com.zerosx.system.dto.SysUserDTO;
import com.zerosx.system.dto.SysUserPageDTO;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.service.IOssFileUploadService;
import com.zerosx.system.service.ISysUserService;
import com.zerosx.system.vo.SysUserPageVO;
import com.zerosx.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IOssFileUploadService ossFileUploadService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAuthFeignServiceApi authFeignServiceApi;

    @Operation(summary = "分页列表")
    @SystemLog(title = "系统用户", btnName = "分页查询", businessType = BusinessType.QUERY)
    @PostMapping("/sys_user/page_list")
    public ResultVO<CustomPageVO<SysUserPageVO>> pageList(@RequestBody RequestVO<SysUserPageDTO> requestVO) {
        return ResultVOUtil.success(sysUserService.pageList(requestVO, true));
    }

    @Operation(summary = "新增")
    @SystemLog(title = "系统用户", btnName = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/sys_user/save")
    public ResultVO<?> add(@Validated @RequestBody SysUserDTO sysUserDTO) {
        return ResultVOUtil.successBoolean(sysUserService.add(sysUserDTO));
    }

    @Operation(summary = "编辑")
    @SystemLog(title = "系统用户", btnName = "编辑", businessType = BusinessType.UPDATE)
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
    @SystemLog(title = "系统用户", btnName = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys_user/delete/{userId}")
    public ResultVO<?> deleteRecord(@PathVariable("userId") Long[] userId) {
        return ResultVOUtil.successBoolean(sysUserService.deleteRecord(userId));
    }

    @Operation(summary = "导出")
    @SystemLog(title = "系统用户", btnName = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/sys_user/export")
    public void operatorExport(@RequestBody RequestVO<SysUserPageDTO> requestVO, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        CustomPageVO<SysUserPageVO> pages = sysUserService.pageList(requestVO, false);
        EasyExcelUtil.writeExcel(response, pages.getList(), SysUserPageVO.class);
        log.debug("【{}】执行导出{}条 耗时:{}ms", ZerosSecurityContextHolder.getUserName(), pages.getTotal(), System.currentTimeMillis() - t1);
    }

    @Operation(summary = "查询登录用户信息")
    @PostMapping("/sys_user/query_login_user")
    public ResultVO<CustomUserDetails> queryLoginUser(@RequestBody UserLoginDTO userLoginDTO) {
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
    @PostMapping("/sys_user/profile/avatar")
    public ResultVO<Map<String, Object>> getProfile(@RequestParam("avatarfile") MultipartFile file) {
        OssObjectVO ossObjectVO = ossFileUploadService.upload(file);
        if (ossObjectVO == null || StringUtils.isBlank(ossObjectVO.getObjectViewUrl())) {
            throw new BusinessException("头像上传失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("imgUrl", ossObjectVO.getObjectViewUrl());
        LambdaUpdateWrapper<SysUser> upqw = Wrappers.lambdaUpdate(SysUser.class);
        upqw.eq(SysUser::getUserName, ZerosSecurityContextHolder.getUserName());
        upqw.set(SysUser::getAvatar, ossObjectVO.getObjectName());
        sysUserService.update(upqw);
        return ResultVOUtil.success(map);
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
    @GetMapping("/sys_user/add_test_users/{num}")
    public ResultVO<?> test001(@PathVariable("num") Integer num) {
        for (Integer i = 1; i <= num; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setUserCode(IdGenerator.getIdStr());
            sysUser.setPassword(passwordEncoder.encode("Admin123"));
            sysUser.setUserName("zeros" + i);
            sysUser.setNickName("测试账号" + i);
            sysUser.setEmail("1232424" + String.format("%08d", i) + "@qq.com");
            sysUser.setPhoneNumber("136" + String.format("%08d", i));
            sysUser.setUserType(UserTypeEnum.TENANT_OPERATOR.getCode());
            sysUser.setDeptId(115L);
            sysUser.setSex("2");
            sysUser.setOperatorId("000000");
            sysUser.setRemark("测试账号");
            sysUserService.save(sysUser);
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
