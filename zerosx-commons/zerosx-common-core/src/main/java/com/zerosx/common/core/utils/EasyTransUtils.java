package com.zerosx.common.core.utils;

import com.zerosx.common.base.anno.Trans;
import com.zerosx.common.core.translation.impl.AbsTranslationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DictTransUtils
 * @Description 数据字典等翻译工具类
 * @Author javacctvnews
 * @Date 2023/5/22 21:29
 * @Version 1.0
 */
@Slf4j
public class EasyTransUtils {

    /**
     * 类需要翻译的字段集合的缓存
     */
    private static final Map<String, Set<Field>> enumsFieldsCacheMap = new ConcurrentHashMap<>();

    /**
     * 单个对象翻译
     *
     * @param t
     * @param <T>
     */
    public static <T> void easyTrans(T t) {
        if (t == null) {
            return;
        }
        Class<?> targetClazz = t.getClass();
        Set<Field> cacheTransFields = enumsFieldsCacheMap.get(targetClazz.getName());
        //缓存为空
        if (cacheTransFields == null) {
            cacheTransFields = new HashSet<>();
            Field[] fields = targetClazz.getDeclaredFields();
            for (Field field : fields) {
                Trans trans = field.getAnnotation(Trans.class);
                if (trans != null) {
                    cacheTransFields.add(field);
                }
            }
            //放入缓存 为空也放入
            enumsFieldsCacheMap.put(targetClazz.getName(), cacheTransFields);
            //log.debug("从Class中获取【{}】需要翻译的字段集合{}个", targetClazz.getName(), transSet.size());
        }
        //没有字段需要翻译
        if (CollectionUtils.isEmpty(cacheTransFields)) {
            return;
        }
        easyTrans(t, targetClazz, cacheTransFields);
    }


    /**
     * list集合翻译
     *
     * @param list
     * @param <T>
     */
    public static <T> void easyTrans(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        T target = list.get(0);
        Class<?> targetClazz = target.getClass();
        Set<Field> cacheTransFields = enumsFieldsCacheMap.get(targetClazz.getName());
        //缓存为空
        if (cacheTransFields == null) {
            cacheTransFields = new HashSet<>();
            Field[] fields = targetClazz.getDeclaredFields();
            for (Field field : fields) {
                Trans trans = field.getAnnotation(Trans.class);
                if (trans != null) {
                    cacheTransFields.add(field);
                }
            }
            //放入缓存
            enumsFieldsCacheMap.put(targetClazz.getName(), cacheTransFields);
        }
        //没有字段需要翻译
        if (CollectionUtils.isEmpty(cacheTransFields)) {
            return;
        }
        //逐条翻译
        long t1 = System.currentTimeMillis();
        Set<Field> finalCacheTransFields = cacheTransFields;
        list.forEach((e) -> easyTrans(e, targetClazz, finalCacheTransFields));
        log.debug("执行【{}】翻译 条数:{} 耗时{}ms", targetClazz.getName(), list.size(), System.currentTimeMillis() - t1);
    }

    /**
     * 单个对象翻译
     *
     * @param t
     * @param targetClazz
     * @param fields
     * @param <T>
     */
    public static <T> void easyTrans(T t, Class<?> targetClazz, Set<Field> fields) {
        if (t == null) {
            return;
        }
        Trans trans;
        for (Field field : fields) {
            trans = field.getAnnotation(Trans.class);
            if (trans != null) {
                transAnnoTranslation(t, targetClazz, field, trans);
            }
        }
    }

    /**
     * 数据字典翻译
     *
     * @param t
     * @param targetClazz
     * @param field
     * @param trans
     * @param <T>
     */
    @SneakyThrows
    private static <T> void transAnnoTranslation(T t, Class<?> targetClazz, Field field, Trans trans) {
        //字典类型的编码
        String dictCode = trans.key();
        //获取注解字段的值
        field.setAccessible(true);
        Object code = field.get(t);
        //为空时不翻译
        if (ObjectUtils.isEmpty(code)) {
            return;
        }
        String dictValue = (String) AbsTranslationService.transBeanCache.get(trans.type()).translation(code, dictCode);
        if (StringUtils.isBlank(dictValue)) {
            return;
        }
        //设置ref字段的值
        String ref = trans.ref();
        if (StringUtils.isNotBlank(ref)) {
            invokerValue(targetClazz.getDeclaredField(ref), t, dictValue);
        }
        //设置refs字段的值
        String[] refs = trans.refs();
        for (String refsEle : refs) {
            invokerValue(targetClazz.getDeclaredField(refsEle), t, dictValue);
        }
    }

    @SneakyThrows
    private static <T> void invokerValue(Field targetClazz, T t, String dictValue) {
        targetClazz.setAccessible(true);
        targetClazz.set(t, dictValue);
    }


}
