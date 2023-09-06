package com.zerosx.common.core.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author javacctvnews
 * @description
 * @date Created in 2021/1/21 14:26
 * @modify by
 */
@Slf4j
@AutoConfiguration
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({TaskExecutionProperties.class})
public class DefaultAsyncExecutorConfig {

    @Autowired
    private TaskExecutionProperties taskExecutionProperties;

    @Bean
    public Executor taskExecutor() {
        //自定义线程池实例
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        // 设置核心线程数 CPU核数+1
        executor.setCorePoolSize(pool.getCoreSize());
        // 设置最大线程数
        executor.setMaxPoolSize(pool.getMaxSize());
        // 设置队列容量
        executor.setQueueCapacity(pool.getQueueCapacity());
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds((int) pool.getKeepAlive().getSeconds());
        // 设置默认线程名称
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        /**
         * 设置拒绝策略
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 传递日志tranceId的装饰器
        executor.setTaskDecorator(new CustomDecorator());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        log.debug("线程池 Executor initialize() 结束");
        //return TtlExecutors.getTtlExecutor(executor);
        return executor;
    }

}
