package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.ResizableCapacityLinkedBlockIngQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 最底层真正修改线程池参数的工具
 */
@Slf4j
public class ThreadPoolDynamicRefresh {

    public static void refreshDynamicPool(String content) {

        PoolParameterInfo parameter = JSON.parseObject(content, PoolParameterInfo.class);
        log.info("[🔥] Start refreshing configuration. content :: {}", parameter);
        refreshDynamicPool(parameter.getTpId(), parameter.getCoreSize(), parameter.getMaxSize(), parameter.getQueueType(), parameter.getCapacity(), parameter.getKeepAliveTime(), parameter.getRejectedType());
    }

    public static void refreshDynamicPool(String threadPoolId, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime, Integer rejectedType) {
        ThreadPoolExecutor beforeExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        int originalCoreSize = beforeExecutor.getCorePoolSize();
        int originalMaximumPoolSize = beforeExecutor.getMaximumPoolSize();
        // TODO: 2024/6/11 后续待修改
        int originalQueryType = -1;
        int originalCapacity = beforeExecutor.getQueue().remainingCapacity() + beforeExecutor.getQueue().size();
        long originalKeepAliveTime = beforeExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS);
        int originalRejectedType = rejectedType;

        changePoolInfo(beforeExecutor, coreSize, maxSize, queueType, capacity, keepAliveTime, rejectedType);

        ThreadPoolExecutor afterExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        log.info("[🔥 {}] Changed thread pool. coreSize :: [{}], maxSize :: [{}], queueType :: [{}], capacity :: [{}], keepAliveTime :: [{}], rejectedType :: [{}]",
                threadPoolId.toUpperCase(),
                String.format("%s=>%s", originalCoreSize, afterExecutor.getCorePoolSize()),
                String.format("%s=>%s", originalMaximumPoolSize, afterExecutor.getMaximumPoolSize()),
                String.format("%s=>%s", originalQueryType, queueType),
                String.format("%s=>%s", originalCapacity, (afterExecutor.getQueue().remainingCapacity() + afterExecutor.getQueue().size())),
                String.format("%s=>%s", originalKeepAliveTime, afterExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS)),
                String.format("%s=>%s", originalRejectedType, rejectedType));
    }

    public static void changePoolInfo(ThreadPoolExecutor executor, Integer coreSize, Integer maxSize, Integer queueType, Integer capacity, Integer keepAliveTime, Integer rejectedType) {
        if (coreSize != null) {
            executor.setCorePoolSize(coreSize);
        }

        if (maxSize != null) {
            executor.setMaximumPoolSize(maxSize);
        }

        if (capacity != null && Objects.equals(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE.type, queueType)) {
            if (executor.getQueue() instanceof ResizableCapacityLinkedBlockIngQueue) {
                ResizableCapacityLinkedBlockIngQueue queue = (ResizableCapacityLinkedBlockIngQueue) executor.getQueue();
                queue.setCapacity(capacity);
            } else {
                log.warn("[Pool change] The queue length cannot be modified. Queue type mismatch. Current queue type :: {}", executor.getQueue().getClass().getSimpleName());
            }
        }

        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }
    }
}
