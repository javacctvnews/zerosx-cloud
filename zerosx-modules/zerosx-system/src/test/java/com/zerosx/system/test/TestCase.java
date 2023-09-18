package com.zerosx.system.test;

import com.zerosx.system.SystemApplication;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.mapper.ISysUserMapper;
import com.zerosx.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = SystemApplication.class)
public class TestCase {

    @Autowired
    private ISysUserMapper sysUserMapper;


    @Test
    public void test01() {
        String userName = "user01";
        String phone = "19829929293";
        SysUser sysUser = sysUserMapper.selectLoginSysUser(userName, phone);
        log.debug("查询结果:{}", JacksonUtil.toJSONString(sysUser));
    }


}
