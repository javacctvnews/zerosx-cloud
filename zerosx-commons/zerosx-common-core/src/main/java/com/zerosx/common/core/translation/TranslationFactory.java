package com.zerosx.common.core.translation;

import com.zerosx.common.base.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TranslationFactory
 * <p>ITranslationService的bean
 *
 * @author: javacctvnews
 * @create: 2023-09-07 15:51
 **/
@Slf4j
public abstract class TranslationFactory {

    private static final Map<String, ITranslationService> transBeanCache = new ConcurrentHashMap<>();

    public static ITranslationService getTranslation(String beanName) {
        ITranslationService translationService = transBeanCache.get(beanName);
        if (translationService != null) {
            return translationService;
        }
        try {
            translationService = (ITranslationService) SpringUtils.getBean(beanName);
        } catch (Exception e) {
            throw new RuntimeException("翻译类型【" + beanName + "】的ITranslationService实现不存在");
        }
        transBeanCache.put(beanName, translationService);
        return translationService;
    }

}
