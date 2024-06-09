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
        ThreadPoolExecutor executor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        log.info("[✈] Original thread pool. coreSize :: {}, maxSize :: {}, queueType :: {}, capacity :: {}, keepAliveTime :: {}, rejectedType:: {}", executor.getCorePoolSize(), executor.getMaximumPoolSize(), queueType, executor.getQueue().remainingCapacity(), executor.getKeepAliveTime(TimeUnit.MILLISECONDS), executor.getRejectedExecutionHandler().toString());

        changePoolInfo(executor, coreSize, maxSize, queueType, capacity, keepAliveTime, rejectedType);

        ThreadPoolExecutor afterExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        log.info("[🚀] Changed thread pool. coreSize :: {}, maxSize :: {}, queueType :: {}, capacity :: {}, keepAliveTime :: {}, rejectedType:: {}", afterExecutor.getCorePoolSize(), afterExecutor.getMaximumPoolSize(), queueType, afterExecutor.getQueue().remainingCapacity(), afterExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS), afterExecutor.getRejectedExecutionHandler().toString());
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
