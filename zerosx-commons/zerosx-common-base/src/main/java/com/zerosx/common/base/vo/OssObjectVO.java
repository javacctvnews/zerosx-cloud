package com.zerosx.common.base.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * oss统一返回的对象
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OssObjectVO {

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 对象查看路径 eTag
     */
    private String objectPath;

    /**
     * 文件查看URL
     */
    private String objectViewUrl;
    /**
     * 签名过期的时间
     */
    private Date expiration;

    public Boolean expired() {
        long diff = expiration.getTime() - System.currentTimeMillis();
        return diff <= 0;
    }

}
