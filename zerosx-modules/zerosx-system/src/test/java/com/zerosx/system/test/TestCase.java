package com.zerosx.system.test;

import com.zerosx.sms.core.SmsFactory;
import com.zerosx.sms.core.config.JuheConfig;
import com.zerosx.sms.enums.SupplierTypeEnum;
import com.zerosx.sms.model.SmsRequest;
import com.zerosx.sms.model.SmsResponse;
import com.zerosx.system.SystemApplication;
import com.zerosx.system.service.ISysDictDataService;
import com.zerosx.system.service.ISysUserService;
import org.junit.jupiter.api.Test;
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

    @Test
    public void test001() {
        JuheConfig juheConfig = JuheConfig.builder().build();
        juheConfig.setKey("12r23423424");
        juheConfig.setDomainAddress("http://v.juhe.cn/sms/send");
        SmsResponse smsResponse = SmsFactory.createSmsClient(SupplierTypeEnum.JUHE, juheConfig).sendMessage(new SmsRequest());


    }

}
