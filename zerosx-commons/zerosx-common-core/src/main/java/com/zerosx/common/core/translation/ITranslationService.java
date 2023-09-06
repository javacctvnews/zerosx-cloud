package com.zerosx.common.core.translation;

/**
 * @ClassName ITranslationService
 * @Description 翻译接口
 * @Author javacctvnews
 * @Date 2023/5/26 10:42
 * @Version 1.0
 */
public interface ITranslationService<T> {

    /**
     * 翻译的类型
     *
     * @return 翻译类型code
     */
    String translationType();

    /**
     * 翻译
     *
     * @param key 需要被翻译的键(不为空)
     * @return 返回键对应的值
     */
    T translation(Object key, String other);


}
