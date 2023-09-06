package com.zerosx.auth.service.userdetails;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 授权用户接口
 */
public interface ICustomUserDetailsService extends UserDetailsService {

    /**
     * 鉴权用户类型 扩展不同数据库表用户鉴权
     *
     * @return
     */
    String authUserType();

    /**
     * 按手机号码查询用户
     *
     * @param mobilePhone
     * @return
     */
    UserDetails loadUserByMobile(String mobilePhone);

}
