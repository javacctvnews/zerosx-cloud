package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OssTranslationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-04 16:10
 **/
@Service
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
        return getRedissonOpService().get(ossFileKey);
    }

    @Override
    protected ResultVO<String> getFeignService(String key) throws Exception {
        return getAsyncFeignService().getObjectViewUrl(key).get();
    }

}
