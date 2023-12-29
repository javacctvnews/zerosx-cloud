//package com.zerosx.dynamictp;
//
//import org.dromara.dynamictp.core.DtpRegistry;
//
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * DtpExecutor
// * <p>
// *
// * @author: javacctvnews
// * @create: 2023-12-21 15:30
// **/
//public final class DtpExecutor {
//
//    public static ScheduledExecutorService scheduledDtpExecutor() {
//        return scheduledDtpExecutor("scheduled12");
//    }
//
//    public static ScheduledExecutorService scheduledDtpExecutor(String name) {
//        return (ScheduledExecutorService) DtpRegistry.getExecutor(name);
//    }
//
//    public static ThreadPoolExecutor threadPoolExecutor() {
//        return threadPoolExecutor("system");
//    }
//
//    public static ThreadPoolExecutor threadPoolExecutor(String name) {
//        return (ThreadPoolExecutor) DtpRegistry.getExecutor(name);
//    }
//
//}
