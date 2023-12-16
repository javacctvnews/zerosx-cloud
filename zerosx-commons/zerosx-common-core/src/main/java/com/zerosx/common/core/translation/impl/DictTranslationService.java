package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.vo.I18nSelectOptionVO;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.base.constants.ZCache;
import com.zerosx.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName DictTranslationService
 * @Description 数据字典的翻译
 * @Author javacctvnews
 * @Date 2023/5/26 10:45
 * @Version 1.0
 */
@Slf4j
public class DictTranslationService extends AbsTranslationService<String> {

    @Override
    public String translationType() {
        return TranslConstants.DICT;
    }

    @Override
    public String translation(Object value, String dictType) {
        Object translation = translation(dictType);
        List<I18nSelectOptionVO> objectMap = JacksonUtil.toList(String.valueOf(translation), I18nSelectOptionVO.class);
        if (CollectionUtils.isNotEmpty(objectMap)) {
            Map<Object, String> map = objectMap.stream().collect(Collectors.toMap(I18nSelectOptionVO::getValue, I18nSelectOptionVO::getLabel));
            return map.get(value);
        }
        return EMPTY;
    }

    @Override
    protected String getRedissonCache(String key) {
        return getRedissonOpService().get(ZCache.DICT_DATA.key(key));
    }

    @Override
    protected ResultVO<List<I18nSelectOptionVO>> getFeignService(String dictType) throws Exception {
        return getAsyncFeignService().getDictList(dictType).get();
    }

}
