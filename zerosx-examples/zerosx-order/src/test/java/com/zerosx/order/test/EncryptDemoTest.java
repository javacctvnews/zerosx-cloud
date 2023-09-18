package com.zerosx.order.test;

import com.zerosx.common.core.utils.IdGenerator;
import com.zerosx.order.OrderApplication;
import com.zerosx.order.entity.UserOrder;
import com.zerosx.order.service.IUserOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * EncryptDemoTest
 * <p> 数据加解密使用示例
 *
 * @author: javacctvnews
 * @create: 2023-09-15 11:44
 **/
@SpringBootTest(classes = OrderApplication.class)
public class EncryptDemoTest {


    @Autowired
    private IUserOrderService userOrderService;


    @Test
    public void insert(){
        UserOrder uo = new UserOrder();
        uo.setEmail("12454@gmail.com");
        uo.setAmount(100d);
        uo.setUserId(IdGenerator.getIdStr());
        uo.setCount(10);
        uo.setOperatorId("000000");
        uo.setCommodityCode("1920");
        uo.setNickName("admin123");
        uo.setIdCard("122922");
        uo.setOrderNo(IdGenerator.getIdStr());
        uo.setPhone("18800002222");
        uo.setStatus("0");
        userOrderService.save(uo);
    }

}
