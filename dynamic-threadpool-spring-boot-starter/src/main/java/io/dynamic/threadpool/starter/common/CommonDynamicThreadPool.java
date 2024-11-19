package io.dynamic.threadpool.starter.common;


import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedPolicies;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 公共线程池生产者
 */
@Slf4j
public class CommonDynamicThreadPool {

    public static DynamicThreadPoolExecutor getInstance(String threadPoolId) {
        DynamicThreadPoolExecutor poolExecutor = (DynamicThreadPoolExecutor) ThreadPoolBuilder.builder()
                .dynamicPool()
                .threadFactory(threadPoolId)
                .poolThreadSize(3, 5)
                .keepAliveTime(1000, TimeUnit.SECONDS)
                .rejected(RejectedPolicies.runsOldestTaskPolicy())
                .alarmConfig(1, 80, 80)
                .workQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE, 512)
                .build();
        return poolExecutor;
    }

}
