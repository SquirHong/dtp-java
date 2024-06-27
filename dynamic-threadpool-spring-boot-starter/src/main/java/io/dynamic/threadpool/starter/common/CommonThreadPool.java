package io.dynamic.threadpool.starter.common;


import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedPolicies;

import java.util.concurrent.*;

/**
 * 公共线程池生产者
 */
public class CommonThreadPool {

    public static CustomThreadPoolExecutor getInstance(String threadPoolId) {
        CustomThreadPoolExecutor poolExecutor = (CustomThreadPoolExecutor) ThreadPoolBuilder.builder()
                .isCustomPool(true)
                .threadPoolId(threadPoolId)
                .threadFactory(threadPoolId)
                .poolThreadSize(3, 5)
                .keepAliveTime(1000, TimeUnit.SECONDS)
                .rejected(RejectedPolicies.runsOldestTaskPolicy())
                .alarmConfig(true, 80, 80)
                .workQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE, 512)
                .build();
        return poolExecutor;
    }

}
