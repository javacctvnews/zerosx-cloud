package com.zerosx.order.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.order.OrderApplication;
import com.zerosx.order.dto.UserDTO;
import com.zerosx.order.entity.User;
import com.zerosx.order.mapper.IUserMapper;
import com.zerosx.order.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IUserService userService;
    @Autowired
    private IUserMapper userMapper;

    /*mybatis-plus常用方法*/
    @Test
    public void insert() {
        User user = new User();
        user.setNickName("张三");
        user.setUserName("张三丰");
        user.setPhone("16688889999");
        user.setEmail("16688889999@qq.com");
        user.setCardId("13899000292920202233");
        user.setAddress("武当山");
        userService.save(user);
        userMapper.insert(user);
    }

    @Test
    public void update() {
        User dbUser = userService.getById(1);
        System.out.println("dbUser = " + JacksonUtil.toJSONString(dbUser));
        if (dbUser != null) {
            dbUser.setPhone("16688883333");
            userService.updateById(dbUser);
        }
        User dbUser2 = userService.getById(1);
        System.out.println("dbUser = " + JacksonUtil.toJSONString(dbUser2));
    }

    /*对加密字段全词匹配查询*/
    @Test
    public void query() {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
        qw.eq(User::getUserName, "张三丰");//前端输入明文
        List<User> list = userService.list(qw);
        System.out.println("参数：" + JacksonUtil.toJSONString(qw));
        for (User user : list) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
    }

    @Test
    public void delete() {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
        qw.eq(User::getUserName, "张三丰");//前端输入明文
        boolean remove = userService.remove(qw);
        System.out.println("remove = " + remove);
    }

    @Test
    public void page() {
        Page<User> page = new Page<>(1, 10);
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery(User.class);
        qw.eq(User::getUserName, "张三丰");//前端输入明文
        Page<User> pageResult = userMapper.selectPage(page, qw);
        List<User> records = pageResult.getRecords();
        long total = pageResult.getTotal();
        System.out.println("参数：" + JacksonUtil.toJSONString(qw));
        System.out.println("总页数：" + total);
        for (User user : records) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
    }


    @Test
    public void queryDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone("16688883333");
        List<User> users = userMapper.queryList(userDTO);
        //参数不应该发生改变
        System.out.println("参数：" + JacksonUtil.toJSONString(userDTO));
        for (User user : users) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
    }

    @Test
    public void queryArray() {
        List<String> phones = new ArrayList<>();
        phones.add("16688883333");
        phones.add("16688883334");
        //入参是集合类型
        List<User> users = userMapper.queryByPhones(phones);
        //参数不应该发生改变
        System.out.println("参数：" + JacksonUtil.toJSONString(phones));
        for (User user : users) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
        String[] phoneArr = new String[]{"16688883333", "16688883334"};
        //入参是数组
        List<User> users2 = userMapper.queryByPhoneArr(phoneArr);
        //参数不应该发生改变
        System.out.println("参数：" + JacksonUtil.toJSONString(phoneArr));
        for (User user : users2) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
    }

    @Test
    public void queryPhones() {
        //查询结果是集合
        List<String> phones = userMapper.queryPhones();
        for (String phone : phones) {
            System.out.println("phone：" + phone);
        }
    }


    @Test
    public void queryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "16688889999");
        map.put("cardId", "13899000292920202233");
        map.put("email", "16688889999@qq.com");
        map.put("nickName", "三");
        //查询结果是集合
        List<User> users = userMapper.queryByMap(map);
        for (User user : users) {
            System.out.println(JacksonUtil.toJSONString(user));
        }
    }


}
