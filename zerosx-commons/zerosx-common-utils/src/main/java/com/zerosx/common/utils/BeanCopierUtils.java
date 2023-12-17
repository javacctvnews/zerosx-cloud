package com.zerosx.common.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanCopier
 * Bean拷贝 大数据量下体现出速度快、性能高
 */
public class BeanCopierUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanCopierUtils.class);

    private BeanCopierUtils() {
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
    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass, boolean useConverter) {
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
            return constructorAccess;
        } catch (Exception e) {
            throw new RuntimeException("Create new instance failed：" + targetClass, e);
        }
    }

    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * 对象拷贝
     *
     * @param source      源
     * @param targetClass 目标
     * @return target
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass) {
        return copyProperties(source, targetClass, null);
    }

    /**
     * 对象拷贝
     *
     * @param source      源
     * @param targetClass 目标
     * @param converter   Converter
     * @return target
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, Converter converter) {
        if (source == null) {
            return null;
        }
        T t = null;
        try {
            t = getConstructorAccess(targetClass).newInstance();
            BeanCopier copier = getBeanCopier(source.getClass(), targetClass, converter != null);
            copier.copy(source, t, converter);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * list拷贝
     *
     * @param sourceList  源list
     * @param targetClass 目标class
     * @return List<T>
     */
    public static <R, T> List<T> copyProperties(List<R> sourceList, Class<T> targetClass) {
        return copyProperties(sourceList, targetClass, null);
    }



    /**
     * list拷贝
     *
     * @param sourceList  源list
     * @param targetClass 目标class
     * @param converter   Converter
     * @return list
     */
    public static <R, T> List<T> copyProperties(List<R> sourceList, Class<T> targetClass, Converter converter) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        BeanCopier beanCopier = getBeanCopier(sourceList.get(0).getClass(), targetClass, converter != null);
        List<T> resultList = new ArrayList<>(sourceList.size());
        long t1 = System.currentTimeMillis();
        T t;
        for (Object o : sourceList) {
            t = constructorAccess.newInstance();
            beanCopier.copy(o, t, converter);
            resultList.add(t);
        }
        log.debug("拷贝{}条，耗时{}ms", resultList.size(), System.currentTimeMillis() - t1);
        return resultList;
    }


    public static <R, T> Collection<T> copyProperties(Collection<R> sourceList, Class<T> targetClass) {
        return copyProperties(sourceList, targetClass, null);
    }



    public static <R, T> Collection<T> copyProperties(Collection<R> sourceList, Class<T> targetClass, Converter converter) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        BeanCopier beanCopier = getBeanCopier(sourceList.iterator().next().getClass(), targetClass, converter != null);
        Collection<T> resultList = new ArrayList<>(sourceList.size());
        long t1 = System.currentTimeMillis();
        T t;
        for (Object o : sourceList) {
            t = constructorAccess.newInstance();
            beanCopier.copy(o, t, converter);
            resultList.add(t);
        }
        log.debug("拷贝{}条，耗时{}ms", resultList.size(), System.currentTimeMillis() - t1);
        return resultList;
    }
}