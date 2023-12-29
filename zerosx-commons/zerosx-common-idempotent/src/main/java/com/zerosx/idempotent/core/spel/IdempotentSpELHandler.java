package com.zerosx.idempotent.core.spel;

import com.zerosx.idempotent.config.IdempotentProperties;
import com.zerosx.idempotent.core.AbsIdempotentExecuteHandler;
import com.zerosx.idempotent.core.IdempotentContext;
import com.zerosx.common.utils.SpELUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;

/**
 * 参数方式幂等实现接口
 */
@Slf4j
public class IdempotentSpELHandler extends AbsIdempotentExecuteHandler {

    private final static String SpEL_PREFIX = "SpEL:";

    public IdempotentSpELHandler(IdempotentProperties idempotentProperties, RedissonClient redissonClient) {
        super(idempotentProperties, redissonClient);
    }


    @Override
    protected IdempotentContext buildContext(ProceedingJoinPoint joinPoint) {
        IdempotentContext context = buildDefaultContext(joinPoint);
        String lockKey = (String) SpELUtils.parse(context.getIdempotent().spEL(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        context.setLockKey(lockKey(SpEL_PREFIX + lockKey));
        return context;
    }


}
