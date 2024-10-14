package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.alarm.ThreadPoolAlarmManage;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
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
        ThreadPoolAlarmManage.sendPoolConfigChange(parameter);
        refreshDynamicPool(parameter);
    }

    public static void refreshDynamicPool(PoolParameterInfo parameter) {
        String threadPoolId = parameter.getTpId();
        ThreadPoolExecutor beforeExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();

        int originalCoreSize = beforeExecutor.getCorePoolSize();
        int originalMaximumPoolSize = beforeExecutor.getMaximumPoolSize();
        String originalQuery = beforeExecutor.getQueue().getClass().getSimpleName();
        int originalCapacity = beforeExecutor.getQueue().remainingCapacity() + beforeExecutor.getQueue().size();
        long originalKeepAliveTime = beforeExecutor.getKeepAliveTime(TimeUnit.SECONDS);
        String originalRejected = beforeExecutor.getRejectedExecutionHandler().getClass().getSimpleName();

        changePoolInfo(beforeExecutor, parameter);

        ThreadPoolExecutor afterExecutor = GlobalThreadPoolManage.getExecutorService(threadPoolId).getPool();
        log.info("[🔥 {}] Changed thread pool. \ncoreSize :: [{}], maxSize :: [{}], queueType :: [{}], capacity :: [{}], keepAliveTime :: [{}], rejectedType :: [{}]",
                threadPoolId.toUpperCase(),
                String.format("%s=>%s", originalCoreSize, afterExecutor.getCorePoolSize()),
                String.format("%s=>%s", originalMaximumPoolSize, afterExecutor.getMaximumPoolSize()),
                String.format("%s=>%s", originalQuery, QueueTypeEnum.getBlockingQueueNameByType(parameter.getQueueType())),
                String.format("%s=>%s", originalCapacity, (afterExecutor.getQueue().remainingCapacity() + afterExecutor.getQueue().size())),
                String.format("%s=>%s", originalKeepAliveTime, afterExecutor.getKeepAliveTime(TimeUnit.SECONDS)),
                String.format("%s=>%s", originalRejected, parameter.getRejectedType()));
    }

    public static void changePoolInfo(ThreadPoolExecutor executor, PoolParameterInfo parameter) {
        if (parameter.getCoreSize() != null) {
            executor.setCorePoolSize(parameter.getCoreSize());
        }

        if (parameter.getMaxSize() != null) {
            executor.setMaximumPoolSize(parameter.getMaxSize());
        }

        if (parameter.getCapacity() != null && Objects.equals(QueueTypeEnum.RESIZABLE_LINKED_BLOCKING_QUEUE.type, parameter.getQueueType())) {
            // TODO: 2024/6/28 这里会出现tpye是 RESIZABLE_LINKED_BLOCKING_QUEUE，但是实际上队列不是 ResizableCapacityLinkedBlockIngQueue 的情况？
            if (executor.getQueue() instanceof ResizableCapacityLinkedBlockIngQueue) {
                ResizableCapacityLinkedBlockIngQueue queue = (ResizableCapacityLinkedBlockIngQueue) executor.getQueue();
                queue.setCapacity(parameter.getCapacity());
            } else {
                log.warn("[Pool change] The queue length cannot be modified. Queue type mismatch. Current queue type :: {}", executor.getQueue().getClass().getSimpleName());
            }
        }

        if (parameter.getKeepAliveTime() != null) {
            executor.setKeepAliveTime(parameter.getKeepAliveTime(), TimeUnit.SECONDS);
        }

        if (parameter.getRejectedType() != null) {
            executor.setRejectedExecutionHandler(RejectedTypeEnum.createPolicy(parameter.getRejectedType()));
        }

    }
}
