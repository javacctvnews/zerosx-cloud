package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.enums.RedisKeyNameEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName OperatorNameTranslationService
 * @Description 租户ID翻译成名称
 * @Author javacctvnews
 * @Date 2023/5/26 13:05
 * @Version 1.0
 */
@Slf4j
public class OperatorNameTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return CommonConstants.TRANS_OPERATOR_ID;
    }

    @Override
    public String translation(Object key, String other) {
        return translation(key);
    }

    @Override
    protected String getRedissonCache(String key) {
        return getRedissonOpService().hGet(RedisKeyNameEnum.key(RedisKeyNameEnum.OPERATOR, key), "operatorId");
    }

    @Override
    protected ResultVO<?> getFeignService(String key) throws Exception {
        return getAsyncFeignService().getTenantName(key).get();
    }
}
