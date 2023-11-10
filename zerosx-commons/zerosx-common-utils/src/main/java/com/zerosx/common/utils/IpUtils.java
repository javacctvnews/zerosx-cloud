package com.zerosx.common.utils;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * IP工具类
 *
 * @author javacctvnews
 */
public class IpUtils {

    /**
     * 缓存，提高性能
     */
    protected static AsyncCache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)//过期时间
            .maximumSize(1000)//最大条数1000
            .buildAsync();//定义cache

    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);

    private static final String UNKNOWN = "unknown";
    public static final String internalIpStr = "内网IP|内网IP";

    // IP地址查询：
    // https://api.vore.top/api/IPdata?ip=[ip地址]
    //http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=[ip地址]
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=";

    /**
     * 解析IP
     *
     * @param ip IP地址
     * @return 解析结果
     */
    public static String getIpLocation(String ip) {
        String cacheIP = getCaffeineCache(ip);
        if (StringUtils.isNotBlank(cacheIP)) {
            return cacheIP;
        }
        if (internalIp(ip)) {
            cache.put(ip, CompletableFuture.completedFuture(internalIpStr));
            return internalIpStr;
        }
        String res;
        try {
            res = HttpClientUtils.get(IP_URL + ip);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
        Map<Object, Object> map = JacksonUtil.toMap(res);
        if (map == null || map.isEmpty()) {
            return StringUtils.EMPTY;
        }
        String ipStr = (String) map.get("addr");
        cache.put(ip, CompletableFuture.completedFuture(ipStr));
        return ipStr;
    }

    /*public static void main(String[] args) {
        String ipLocation = getIpLocation("192.168.3.4");
        System.out.println("ipLocation = " + ipLocation);
    }*/

    private static boolean internalIp(String ip) {
        if ("127.0.0.1".equals(ip) || ip.startsWith("0")) {
            return true;
        }
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }

    private static boolean internalIp(byte[] addr) {
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

    /**
     * 获取本机的IP地址
     */
    public static String getLocalAddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    @SneakyThrows
    private static String getCaffeineCache(String key) {
        CompletableFuture<String> completableFuture = cache.getIfPresent(key);
        if (completableFuture == null) {
            //log.debug("【获取】Caffeine缓存, {} = 缓存为空", key);
            return StringUtils.EMPTY;
        }
        String cacheValue = completableFuture.get();
        //log.debug("【获取】Caffeine缓存 {} = {}", key, cacheValue);
        return cacheValue;
    }

    private static boolean isEmptyIP(String ip) {
        return StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip);
    }

}