package io.dynamic.threadpool.starter.common;

import io.dynamic.threadpool.common.enums.QueueTypeEnum;
import io.dynamic.threadpool.starter.builder.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedPolicies;

import java.util.concurrent.*;

/**
 * 公共线程池生产者
 */
public class CommonThreadPool {

    public static ThreadPoolExecutor getInstance(String threadPoolId) {
        ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .poolThreadNum(3, 5)
                .keepAliveTime(1000L, TimeUnit.SECONDS)
                .rejected(RejectedPolicies.runsOldestTaskPolicy())
                .workQueue(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE, 512)
                .build();
        return poolExecutor;
    }
}
