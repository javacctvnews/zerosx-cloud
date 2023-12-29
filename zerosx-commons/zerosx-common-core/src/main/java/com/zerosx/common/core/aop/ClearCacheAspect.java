package com.zerosx.common.core.aop;

import com.zerosx.common.core.anno.ClearCache;
import com.zerosx.common.redis.templete.RedissonOpService;
import com.zerosx.common.utils.SpELUtils;
import com.zerosx.dynamictp.ZExecutor;
import com.zerosx.dynamictp.constant.DtpConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ClearCacheAspect
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-25 09:42
 **/
@Slf4j
@Aspect
@Component
public class ClearCacheAspect {

    @Autowired
    private RedissonOpService redissonOpService;

    @Around("@annotation(com.zerosx.common.core.anno.ClearCache)")
    public Object point(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        List<String> keys = new ArrayList<>();
        ClearCache clearCache = null;
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
            clearCache = targetMethod.getAnnotation(ClearCache.class);
            String spELKey = (String) SpELUtils.parse(clearCache.field(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
            keys.add(clearCache.keys() + ":" + (spELKey));
            del(clearCache, keys, false);
            res = joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        }
        del(clearCache, keys, true);
        return res;
    }

    public void del(ClearCache clearCache, List<String> keys, boolean delay) {
        if (clearCache == null || CollectionUtils.isEmpty(keys)) {
            return;
        }
        if (!delay) {
            redissonOpService.del(keys);
            return;
        }
        ScheduledExecutorService executor = ZExecutor.getScheduledExecutor(DtpConstants.SCHEDULED_DYNAMIC_TP);
        executor.schedule(() -> {
            redissonOpService.del(keys);
            log.debug("执行删除key:{}", String.join(",", keys));
        }, clearCache.delayTime(), TimeUnit.MILLISECONDS);
    }

}
