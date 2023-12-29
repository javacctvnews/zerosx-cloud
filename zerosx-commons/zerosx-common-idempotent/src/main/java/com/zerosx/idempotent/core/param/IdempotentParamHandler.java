package com.zerosx.idempotent.core.param;

import cn.hutool.crypto.digest.DigestUtil;
import com.zerosx.idempotent.config.IdempotentProperties;
import com.zerosx.idempotent.core.AbsIdempotentExecuteHandler;
import com.zerosx.idempotent.core.IdempotentContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;

/**
 * 参数方式幂等实现接口
 */
@Slf4j
public class IdempotentParamHandler extends AbsIdempotentExecuteHandler {

    public IdempotentParamHandler(IdempotentProperties idempotentProperties, RedissonClient redissonClient) {
        super(idempotentProperties, redissonClient);
    }

    @Override
    protected IdempotentContext buildContext(ProceedingJoinPoint joinPoint) {
        IdempotentContext context = buildDefaultContext(joinPoint);
        String lockKey = String.format("param:path_%s:param_%s", DigestUtil.md5Hex(context.getServletPath()), DigestUtil.md5Hex(context.getRequestParam()));
        context.setLockKey(lockKey(lockKey));
        return context;
    }

}
