package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.vo.OssObjectVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * OssTranslationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-04 16:10
 **/
@Slf4j
public class OssTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return CommonConstants.TRANS_OSS;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String objectName) {
        String ossFileKey = RedisKeyNameEnum.key(RedisKeyNameEnum.OSS_FILE_URL, objectName);
        OssObjectVO ossObjectVO;
        try {
            String ossObjectStr = getRedissonOpService().get(ossFileKey);
            ossObjectVO = JacksonUtil.toObject(ossObjectStr, OssObjectVO.class);
        } catch (Exception e) {
            getRedissonOpService().del(ossFileKey);
            return EMPTY;
        }
        if (ossObjectVO != null && !ossObjectVO.expired()) {
            return ossObjectVO.getObjectViewUrl();
        }
        return EMPTY;
    }

    @Override
    protected ResultVO<String> getFeignService(String key) throws Exception {
        return getAsyncFeignService().getObjectViewUrl(key).get();
    }

}
