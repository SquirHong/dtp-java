package io.dynamic.threadpool.starter.event;


import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 处理线程池变更事件.
 */
public class EventExecutor {

    private static final ExecutorService EVENT_EXECUTOR = ThreadPoolBuilder.builder()
            .threadFactory("event-executor")
            .corePoolNum(Runtime.getRuntime().availableProcessors())
            .maxPoolNum(Runtime.getRuntime().availableProcessors())
            .workQueue(QueueTypeEnum.ARRAY_BLOCKING_QUEUE, 2048)
            .rejected(new ThreadPoolExecutor.CallerRunsPolicy())
            .build();

    /**
     * 发布事件.
     */
    public static void publishEvent(Runnable runnable) {
        EVENT_EXECUTOR.execute(runnable);
    }

}
