package com.zerosx.common.core.utils;

import com.zerosx.common.base.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 客户端工具类
 */
public class ServletUtils {

    public static String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isEmpty(value)) {
            return StringUtils.EMPTY;
        }
        return urlDecode(value);
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF8");
        } catch (UnsupportedEncodingException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF8");
        } catch (UnsupportedEncodingException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取请求token
     */
    public static String getToken(HttpServletRequest request, String name) {
        String token = request.getHeader(name);
        if (StringUtils.isNotEmpty(token) && token.startsWith(CommonConstants.BEARER_TYPE)) {
            token = token.replaceFirst(CommonConstants.BEARER_TYPE, StringUtils.EMPTY).trim();
        }
        return token;
    }

}
