package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.SpringUtils;

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
        return SpringUtils.getBean(RedissonOpService.class).hGet(RedisKeyNameEnum.key(RedisKeyNameEnum.REGION_HASH), key);
    }

    @Override
    protected ResultVO<?> getFeignService(String key) throws Exception {
        return getAsyncFeignService().getAreaName(key).get();
    }

}
