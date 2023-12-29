package com.zerosx.dynamictp;

import org.dromara.dynamictp.core.DtpRegistry;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * DtpExecutor
 * <p>
 * 重新包装线程池获取工具类
 *
 * @author: javacctvnews
 * @create: 2023-12-21 15:30
 **/
public final class ZExecutor {

    /**
     * 通过线程池名称获取执行器
     *
     * @param name 线程池名称
     * @return 执行器
     */
    /*public static Executor getExecutor(String name) {
        return DtpRegistry.getExecutor(name);
    }*/

    public static ThreadPoolExecutor getExecutor(String name) {
        return (ThreadPoolExecutor) DtpRegistry.getExecutor(name);
    }


    /**
     * 通过线程池名称获取Scheduled执行器
     *
     * @param name 线程池名称
     * @return 执行器
     */
    public static ScheduledExecutorService getScheduledExecutor(String name) {
        return (ScheduledExecutorService) DtpRegistry.getDtpExecutor(name);
    }

}
