package io.dynamic.threadpool.starter.common;


import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedPolicies;

import java.util.concurrent.*;

/**
 * 公共线程池生产者
 */
public class CommonThreadPool {

    public static ThreadPoolExecutor getInstance(String threadPoolId) {
        ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
                .isCustomPool(true)
                .threadFactory(threadPoolId)
                .poolThreadSize(3, 5)
                .keepAliveTime(1000, TimeUnit.SECONDS)
                .rejected(RejectedPolicies.runsOldestTaskPolicy())
                .workQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE, 512)
                .build();
        return poolExecutor;
    }

}
