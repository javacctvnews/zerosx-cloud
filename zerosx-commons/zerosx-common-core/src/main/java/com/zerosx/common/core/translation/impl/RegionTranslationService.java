package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.utils.SpringUtils;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;

/**
 * OssTranslationService
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-09-04 16:10
 **/
public class RegionTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return CommonConstants.TRANS_REGION;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String key) {
        return SpringUtils.getBean(RedissonOpService.class).getHashValue(RedisKeyNameEnum.key(RedisKeyNameEnum.REGION_HASH), key);
    }

    @Override
    protected ResultVO<?> getFeignService(String key) throws Exception {
        return getAsyncFeignService().getAreaName(key).get();
    }

}
