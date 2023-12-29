package com.zerosx.idempotent.core;

import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.aspect.IdempotentAspect;
import com.zerosx.idempotent.config.IdempotentProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * AbsIdempotentExecuteHandler
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-18 17:43
 **/
@Slf4j
public abstract class AbsIdempotentExecuteHandler implements IdempotentExecuteHandler {

    private final static String LOCK = "idempotent_lock";
    private final static String PARAM = "idempotent_context";

    protected final IdempotentProperties idempotentProperties;

    protected final RedissonClient redissonClient;

    protected AbsIdempotentExecuteHandler(IdempotentProperties idempotentProperties, RedissonClient redissonClient) {
        this.idempotentProperties = idempotentProperties;
        this.redissonClient = redissonClient;
    }

    /**
     * 构建幂等验证过程中所需要的参数包装器
     *
     * @param joinPoint AOP 方法处理
     * @return 幂等参数包装器
     */
    protected abstract IdempotentContext buildContext(ProceedingJoinPoint joinPoint);

    /**
     * 执行幂等处理逻辑
     *
     * @param joinPoint  AOP 方法处理
     * @param idempotent 幂等注解
     */
    public void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        // 模板方法模式：构建幂等参数包装器
        IdempotentContext idempotentContext = buildContext(joinPoint);
        //log.debug("幂等lockKey：{}", idempotentContext.getLockKey());
        handler(idempotentContext);
    }

    @Override
    public void handler(IdempotentContext context) {
        //log.debug("执行handler()分布式锁上锁，路径:{} 参数:{}", context.getServletPath(), JacksonUtil.toJSONString(context));
        String lockKey = context.getLockKey();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(idempotentProperties.getWaitTime(), idempotentProperties.getLeaseTime(), TimeUnit.SECONDS)) {
                log.error("幂等重复提交，路径:{} 参数:{}", context.getServletPath(), context.getRequestParam());
                throw new RuntimeException(context.getIdempotent().msg());
            }
        } catch (InterruptedException e) {
            log.error("幂等重复提交，路径:{} 参数:{}", context.getServletPath(), context.getRequestParam());
            throw new RuntimeException(context.getIdempotent().msg());
        }
        IdempotentContextHolder.put(LOCK, lock);
        IdempotentContextHolder.put(PARAM, context);
    }

    @Override
    public void postProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContextHolder.getKey(LOCK);
        } finally {
            if (lock != null) {
                IdempotentContext context = (IdempotentContext) IdempotentContextHolder.getKey(PARAM);
                lock.unlock();
                if (context != null) {
                    log.debug("释放redisson分布式锁【{}】，路径：{}", context.getLockKey(), context.getServletPath());
                }
            }
        }
    }

    @SneakyThrows
    protected IdempotentContext buildDefaultContext(ProceedingJoinPoint joinPoint) {
        IdempotentContext context = new IdempotentContext();
        context.setIdempotent(IdempotentAspect.getIdempotent(joinPoint));
        //请求参数
        String paramJSON = JacksonUtil.toJSONString(joinPoint.getArgs());
        context.setRequestParam(paramJSON);
        //请求路径
        String servletPath = getServletPath();
        context.setServletPath(servletPath);
        return context;
    }

    /**
     * @return 获取当前线程上下文 ServletPath
     */
    protected String getServletPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null) {
            throw new RuntimeException("ServletRequestAttributes is null");
        }
        return sra.getRequest().getServletPath();
    }

    protected String lockKey(String lockKey) {
        return idempotentProperties.getLockKeyPrefix() + ":" + lockKey;
    }

}
