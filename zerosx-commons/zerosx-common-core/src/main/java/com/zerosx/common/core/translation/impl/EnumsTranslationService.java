package com.zerosx.common.core.translation.impl;

import com.zerosx.common.base.constants.TranslConstants;
import com.zerosx.common.base.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName EnumsTranslationService
 * @Description 枚举类型的翻译实现
 * @Author javacctvnews
 * @Date 2023/5/26 10:51
 * @Version 1.0
 */
@Slf4j
public class EnumsTranslationService extends AbsTranslationService<String> {

    /**
     * 枚举缓存
     */
    private static final Map<String, Object[]> enumsCacheMap = new ConcurrentHashMap<>();

    @Override
    public String translationType() {
        return TranslConstants.ENUMS;
    }

    @Override
    public String translation(Object fieldVal, String enumClass) {
        Class<?> clz;
        try {
            //根据code获取枚举对应的message
            clz = Class.forName(enumClass);
        } catch (ClassNotFoundException e) {
            log.error("不存在的枚举类【{}】请检查", enumClass);
            return EMPTY;
        }
        Object message = getEnumValue(clz, fieldVal);
        return String.valueOf(message);
    }

    /**
     * 通过枚举的code 得到枚举的name
     * class 枚举
     * code：传入code值   通过code 得到枚举的name
     */
    private static Object getEnumValue(Class<?> clazz, Object code) {
        Object[] enumConstants = enumsCacheMap.get(clazz.getName());
        if (enumConstants == null || enumConstants.length == 0) {
            //返回枚举类的元素，或null如果此Class对象不表示枚举类型。
            enumConstants = clazz.getEnumConstants();
            enumsCacheMap.put(clazz.getName(), enumConstants);
        }
        try {
            for (Object object : enumConstants) {
                //获得对象所声明的公开方法 参数标识方法名称
                Method codeMethod = clazz.getMethod("getCode");
                Method nameMethod = clazz.getMethod("getMessage");
                //执行对象的目标方法
                if (code.equals(codeMethod.invoke(object))) {
                    return nameMethod.invoke(object);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return EMPTY;
    }

    @Override
    protected String getRedissonCache(String key) {
        return null;
    }

    @Override
    protected ResultVO<?> getFeignService(String key) throws Exception {
        return null;
    }

}

