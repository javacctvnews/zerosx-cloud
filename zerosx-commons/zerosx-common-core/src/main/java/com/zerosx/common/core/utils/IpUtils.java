package com.zerosx.common.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.utils.HttpClientUtils;
import com.zerosx.common.base.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP方法
 *
 * @author javacctvnews
 */
@Slf4j
public class IpUtils {

    // IP地址查询：
    // https://api.vore.top/api/IPdata?ip=[ip地址]
    //http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=[ip地址]
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";
    public static final String internalIpStr = "内网IP|内网IP";

    public static String getIpLocation(String ip) {
        if (internalIp(ip)) {
            return "内网IP";
        }
        String res;
        try {
            res = HttpClientUtils.doGet(IP_URL + ip);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
        JsonNode jsonNode = JacksonUtil.parse(res);
        if (jsonNode == null) {
            return StringUtils.EMPTY;
        }
        return jsonNode.get("addr").asText();
    }

    /*public static void main(String[] args) {
        String ipLocation = getIpLocation("192.168.3.4");
        System.out.println("ipLocation = " + ipLocation);
    }*/

    public static boolean internalIp(String ip) {
        if ("127.0.0.1".equals(ip) || ip.startsWith("0")) {
            return true;
        }
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }

    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * 获取客户端IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isEmptyIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (isEmptyIP(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (isEmptyIP(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                    if (isEmptyIP(ip)) {
                        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                        if (isEmptyIP(ip)) {
                            ip = request.getRemoteAddr();
                            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                                // 根据网卡取本机配置的IP
                                ip = getLocalAddr();
                            }
                        }
                    }
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!isEmptyIP(ip)) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static boolean isEmptyIP(String ip) {
        if (StringUtils.isBlank(ip) || CommonConstants.UNKNOWN_STR.equalsIgnoreCase(ip)) {
            return true;
        }
        return false;
    }

    /**
     * 获取本机的IP地址
     */
    public static String getLocalAddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("InetAddress.getLocalHost()-error", e);
        }
        return "";
    }

}