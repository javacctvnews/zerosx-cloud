package com.zerosx.common.core.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.text.MessageFormat.format;

/**
 * BeanCopier
 * Bean拷贝 大数据量下体现出速度快、性能高
 */
@Slf4j
public class BeanCopierUtil {

    private BeanCopierUtil() {
    }

    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    /**
     * getBeanCopier
     *
     * @param sourceClass
     * @param targetClass
     * @param useConverter 是否使用converter，默认false
     * @return
     */
    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass, boolean useConverter) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier;
        if (!BEAN_COPIER_CACHE.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, useConverter);
            BEAN_COPIER_CACHE.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(beanKey);
        }
        return copier;
    }

    /**
     * 两个类的全限定名拼接起来构成Key
     *
     * @param sourceClass
     * @param targetClass
     * @return
     */
    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + targetClass.getName();
    }

    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess = CONSTRUCTOR_ACCESS_CACHE.get(targetClass.getName());
        if (constructorAccess != null) {
            return constructorAccess;
        }
        try {
            constructorAccess = ConstructorAccess.get(targetClass);
            constructorAccess.newInstance();
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.toString(), constructorAccess);
        } catch (Exception e) {
            throw new RuntimeException(format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        return constructorAccess;
    }

    /**
     * signal copy
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target, Converter converter, boolean trans) {
        if (source == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), converter != null);
        copier.copy(source, target, converter);
        if (trans) {
            EasyTransUtils.easyTrans(target);
        }
    }

    /**
     * signal copy
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }


    /**
     * signal copy
     *
     * @param source
     * @param targetClass
     * @param converter
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, Converter converter) {
        if (source == null) {
            return null;
        }
        T t;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        copyProperties(source, t, converter, true);
        return t;
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        return copyProperties(source, targetClass, null);
    }

    /**
     * list copy
     *
     * @param sourceList
     * @param targetClass
     * @param converter
     * @param <T>
     * @return
     */
    public static <T> List<T> copyPropertiesOfList(List<?> sourceList, Class<T> targetClass, Converter converter) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        long t1 = System.currentTimeMillis();
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        List<T> resultList = new ArrayList<>(sourceList.size());
        T t;
        for (Object o : sourceList) {
            try {
                t = constructorAccess.newInstance();
                copyProperties(o, t, converter, false);
                resultList.add(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.debug("执行【{}】拷贝 条数:{} 耗时{}ms", targetClass.getName(), resultList.size(), System.currentTimeMillis() - t1);
        //翻译
        EasyTransUtils.easyTrans(resultList);
        return resultList;
    }

    public static <T> List<T> copyPropertiesOfList(List<?> sourceList, Class<T> targetClass) {
        return copyPropertiesOfList(sourceList, targetClass, null);
    }

}