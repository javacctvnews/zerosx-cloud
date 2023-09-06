package com.zerosx.common.core.interceptor;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.zerosx.common.base.constants.HeadersConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ZerosSecurityContextHolder
 * @Description 获取当前线程变量中的 用户登录名、租户集团ID集合、子租户ID集合等信息。
 * @Author javacctvnews
 * @Date 2023/3/13 13:51
 * @Version 1.0
 */
public class ZerosSecurityContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static Object getObj(String key) {
        Map<String, Object> map = getLocalMap();
        return map.get(key);
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    /**
     * @Description 获取当前登录用户名
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static String getUserName() {
        return get(HeadersConstants.USERNAME);
    }

    /**
     * @Description 获取当前登录用户id
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static Long getUserId() {
        return Long.valueOf(get(HeadersConstants.USERID));
    }

    /**
     * @Description 设置当前登录用户类型
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static void setUserType(String userType) {
        set(HeadersConstants.USERTYPE, userType);
    }

    /**
     * @Description 获取当前登录用户类型
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static String getUserType() {
        return get(HeadersConstants.USERTYPE);
    }

    /**
     * @Description 设置当前登录用户名
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static void setUserName(String username) {
        set(HeadersConstants.USERNAME, username);
    }

    /**
     * @Description 设置当前登录用户ID
     * @Author javacctvnews
     * @Date 2023/3/13 17:34
     */
    public static void setUserId(String userId) {
        set(HeadersConstants.USERID, userId);
    }


    /**
     * @Description 获取当前用户的租户ID集合
     * @Author javacctvnews
     * @Date 2023/3/13 17:35
     */
    public static String getOperatorIds() {
        return get(HeadersConstants.OPERATOR_ID);
    }

    /**
     * @Description 设置当前用户的租户ID集合
     * @Author javacctvnews
     * @Date 2023/3/13 17:35
     */
    public static void setOperatorIds(String operatorId) {
        set(HeadersConstants.OPERATOR_ID, operatorId);
    }


    public static void remove() {
        THREAD_LOCAL.remove();
    }


}
