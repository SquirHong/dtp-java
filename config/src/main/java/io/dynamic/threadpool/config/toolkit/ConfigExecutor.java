package io.dynamic.threadpool.config.toolkit;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.common.executor.ExecutorFactory;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Config Executor.
 */
public class ConfigExecutor {

    // 执行长轮训任务的 Executor
    private static final ScheduledExecutorService LONG_POLLING_EXECUTOR = ExecutorFactory.Managed
            .newSingleScheduledExecutorService("default group",
                    r -> new Thread(r, "long-polling"));

    public static void executeLongPolling(Runnable runnable) {
        LONG_POLLING_EXECUTOR.execute(runnable);
    }

    /**
     * 定时长轮训
     */
    public static ScheduledFuture<?> scheduleLongPolling(Runnable runnable, long period, TimeUnit unit) {
        return LONG_POLLING_EXECUTOR.schedule(runnable, period, unit);
    }

    /**
     * 延迟定时长轮训
     */
    public static void scheduleLongPolling(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        LONG_POLLING_EXECUTOR.scheduleWithFixedDelay(runnable, initialDelay, period, unit);
    }
}
