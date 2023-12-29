package com.zerosx.system.controller;

import com.zerosx.api.auth.IAccessTokenClient;
import com.zerosx.api.resource.IDGenClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.BizTagEnum;
import com.zerosx.common.core.enums.system.UserTypeEnum;
import com.zerosx.common.core.utils.LeafUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.dynamictp.ZExecutor;
import com.zerosx.dynamictp.constant.DtpConstants;
import com.zerosx.system.dto.UserTestDTO;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@RestController
@Tag(name = "DEMO测试")
public class DemoTestController {

    @Autowired
    private IDGenClient idGenClient;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAccessTokenClient authFeignServiceApi;

    @Operation(summary = "创建分布式ID(分段式)-http")
    @GetMapping(value = "/segment_test/{bizTag}")
    public ResultVO<Long> segment1(@PathVariable("bizTag") String bizTag) {
        return idGenClient.segment(bizTag);
    }

    @Operation(summary = "创建分布式ID(分段式)-工具类")
    @GetMapping(value = "/segment_test2/{bizTag}")
    public ResultVO<Long> segment2(@PathVariable("bizTag") String bizTag) {
        Long uid = LeafUtils.uid(bizTag);
        System.out.println(uid);
        return ResultVOUtil.success(uid);
    }

    @Operation(summary = "系统用户-批量新增")
    @PostMapping("/sys_user/batch_add")
    public ResultVO<?> test001(@RequestBody UserTestDTO userTestDTO) throws Exception {
        Integer num = userTestDTO.getNum();
        List<SysUser> srs = new ArrayList<>();
        String admin123 = passwordEncoder.encode("Admin123");
        SysUser sysUser;
        for (Integer i = 1; i <= num; i++) {
            sysUser = new SysUser();
            sysUser.setUserType(UserTypeEnum.TENANT_OPERATOR.getCode());
            sysUser.setId(LeafUtils.uid(BizTagEnum.USER_CODE));
            sysUser.setUserCode(sysUser.getId().toString());
            sysUser.setPassword(admin123);
            sysUser.setUserName(userTestDTO.getPrefix() + i);
            sysUser.setNickName(sysUser.getUserName());
            sysUser.setEmail("66" + String.format("%08d", i) + "@qq.com");
            sysUser.setPhoneNumber("136" + String.format("%08d", i));
            sysUser.setDeptId(1L);
            sysUser.setSex("2");
            sysUser.setOperatorId(userTestDTO.getOperatorId());
            sysUser.setRemark("测试账号" + i);
            srs.add(sysUser);
        }
        List<List<SysUser>> partitions = ListUtils.partition(srs, 1000);
        for (List<SysUser> users : partitions) {
            sysUserService.saveBatch(users);
            Thread.sleep(1500);
        }
        return ResultVOUtil.success();
    }

    @Operation(summary = "系统用户-批量登录")
    @PostMapping("/sys_user/batch_login")
    public ResultVO<?> login(@RequestBody UserTestDTO userTestDTO) throws Exception {
        Integer num = userTestDTO.getNum();
        MultiValueMap<String, String> multiValueMap;
        Executor executor = ZExecutor.getExecutor(DtpConstants.DYNAMIC_TP);
        for (int i = 1; i <= num; i++) {
            multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("client_id", "saas");
            multiValueMap.add("client_secret", "Zeros9999!#@");
            multiValueMap.add("grant_type", "password");
            multiValueMap.add("password", "Admin123");
            multiValueMap.add("user_auth_type", "SysUser");
            multiValueMap.add("username", userTestDTO.getPrefix() + i);
            try {
                MultiValueMap<String, String> finalMultiValueMap = multiValueMap;
                executor.execute(() -> {
                    long t1 = System.currentTimeMillis();
                    ResultVO resultVO = authFeignServiceApi.postAccessToken(finalMultiValueMap);
                    log.debug("登录耗时{}ms 结果:{}", System.currentTimeMillis() - t1, JacksonUtil.toJSONString(resultVO));
                });
            } catch (Exception e) {

            }
            Thread.sleep(userTestDTO.getDelayTime());
        }
        return ResultVOUtil.success();
    }

    @Operation(summary = "系统用户-批量缓存")
    @PostMapping("/sys_user/batch_cache")
    public ResultVO<?> cache(@RequestBody UserTestDTO userTestDTO) throws Exception {
        Integer num = userTestDTO.getNum();
        Executor executor = ZExecutor.getExecutor(DtpConstants.DYNAMIC_TP);
        for (int i = 1; i <= num; i++) {
            int finalI = i;
            executor.execute(() -> {
                sysUserService.currentLoginUser(userTestDTO.getPrefix() + finalI);
            });
            Thread.sleep(userTestDTO.getDelayTime());
        }
        return ResultVOUtil.success();
    }
}
