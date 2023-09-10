package com.zerosx.common.oss.core.client;

import java.util.Date;

/**
 * AbsOssClientService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-08 14:21
 **/
public abstract class AbsOssClientService implements IOssClientService {

    @Override
    public boolean deleteAfterDays(String objectName, int days) {
        //todo 按需实现
        return true;
    }

    protected Date getExpireDate(Long expireTime) {
        return new Date(new Date().getTime() + expireTime * 1000L);
    }

}
