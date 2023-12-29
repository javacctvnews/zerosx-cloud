package com.zerosx.idempotent.aspect;

import com.zerosx.common.utils.SpringUtils;
import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.core.IdempotentContextHolder;
import com.zerosx.idempotent.core.IdempotentExecuteHandler;
import com.zerosx.idempotent.core.param.IdempotentParamHandler;
import com.zerosx.idempotent.core.spel.IdempotentSpELHandler;
import com.zerosx.idempotent.enums.IdempotentTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * IdempotentAspect
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-18 16:43
 **/
@Slf4j
@Aspect
public class IdempotentAspect {

    @Around("@annotation(com.zerosx.idempotent.anno.Idempotent)")
    public Object handleIdempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        Idempotent idempotent = getIdempotent(joinPoint);
        IdempotentExecuteHandler handler = getInstance(idempotent.type());
        try {
            handler.execute(joinPoint, idempotent);
            res = joinPoint.proceed();
        } catch (Throwable e) {
            //log.error(e.getMessage(), e);
            throw e;
        } finally {
            handler.postProcessing();
            IdempotentContextHolder.clean();
        }
        return res;
    }

    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }

    public static IdempotentExecuteHandler getInstance(IdempotentTypeEnum type) {
        IdempotentExecuteHandler result = null;
        switch (type) {
            case PARAM:
                result = SpringUtils.getBean(IdempotentParamHandler.class);
                break;
            case SPEL:
                result = SpringUtils.getBean(IdempotentSpELHandler.class);
                break;
            default:
                break;
        }
        return result;
    }

}
