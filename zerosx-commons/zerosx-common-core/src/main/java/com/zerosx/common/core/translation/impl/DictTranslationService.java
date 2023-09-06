package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.CommonConstants;
import com.zerosx.common.base.utils.JacksonUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.redis.enums.RedisKeyNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName DictTranslationService
 * @Description 数据字典的翻译
 * @Author javacctvnews
 * @Date 2023/5/26 10:45
 * @Version 1.0
 */
@Service
@Slf4j
public class DictTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return CommonConstants.TRANS_DICT;
    }

    @Override
    public String translation(Object value, String dictType) {
        Object translation = translation(dictType);
        Map<String, String> objectMap = JacksonUtil.toMap(translation);
        if (MapUtils.isNotEmpty(objectMap)) {
            return objectMap.get(String.valueOf(value));
        }
        return EMPTY;
    }

    @Override
    protected String getRedissonCache(String key) {
        return getRedissonOpService().get(RedisKeyNameEnum.key(RedisKeyNameEnum.DICT_DATA, key));
    }

    @Override
    protected ResultVO<Map<String, String>> getFeignService(String dictType) throws Exception {
        return getAsyncFeignService().getDictData(dictType).get();
    }

}
