package com.zerosx.system.test;

import com.zerosx.system.SystemApplication;
import com.zerosx.system.service.ISysDictDataService;
import com.zerosx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = SystemApplication.class)
public class TestCase {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private RestTemplate restTemplate;


}
