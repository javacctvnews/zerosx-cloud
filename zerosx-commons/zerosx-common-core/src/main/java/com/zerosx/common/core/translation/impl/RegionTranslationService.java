package com.zerosx.common.core.translation.impl;

import com.zerosx.api.resource.IRegionClient;
import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.constants.ZCache;
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
        return TranslConstants.REGION;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String key) {
        return SpringUtils.getBean(RedissonOpService.class).hGet(ZCache.REGION_HASH.key(), key);
    }

    @Override
    protected ResultVO<?> getFeignService(String key) {
        return SpringUtils.getBean(IRegionClient.class).getAreaName(key);
    }

}
