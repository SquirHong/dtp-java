package io.dynamic.threadpool.starter.handle;


import cn.hutool.core.date.DateUtil;
import io.dynamic.threadpool.common.model.PoolRunStateInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.CalculateUtil;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractThreadPoolRuntime {

    /**
     * Supplement.
     *
     * @param basePoolRunStateInfo
     * @return
     */
    protected abstract PoolRunStateInfo supplement(PoolRunStateInfo basePoolRunStateInfo);

    /**
     * Get pool run state.
     *
     * @param threadPoolId
     * @return
     */
    public PoolRunStateInfo getPoolRunState(String threadPoolId) {
        DynamicThreadPoolWrapper executorService = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        ThreadPoolExecutor pool = executorService.getExecutor();

        // 核心线程数
        int corePoolSize = pool.getCorePoolSize();
        // 最大线程数
        int maximumPoolSize = pool.getMaximumPoolSize();
        // 线程池当前线程数 (有锁)
        int poolSize = pool.getPoolSize();
        // 活跃线程数 (有锁)
        int activeCount = pool.getActiveCount();
        // 同时进入池中的最大线程数 (有锁)
        int largestPoolSize = pool.getLargestPoolSize();
        // 线程池中执行任务总数量 (有锁)
        long completedTaskCount = pool.getCompletedTaskCount();
        // 当前负载
        String currentLoad = CalculateUtil.divide(activeCount, maximumPoolSize) + "%";
        // 峰值负载
        String peakLoad = CalculateUtil.divide(largestPoolSize, maximumPoolSize) + "%";

        BlockingQueue<Runnable> queue = pool.getQueue();
        // 队列元素个数
        int queueSize = queue.size();
        // 队列类型
        String queueType = queue.getClass().getSimpleName();
        // 队列剩余容量
        int remainingCapacity = queue.remainingCapacity();
        // 队列容量
        int queueCapacity = queueSize + remainingCapacity;

        PoolRunStateInfo stateInfo = new PoolRunStateInfo();
        stateInfo.setCoreSize(corePoolSize);
        stateInfo.setTpId(threadPoolId);
        stateInfo.setPoolSize(poolSize);
        stateInfo.setMaximumSize(maximumPoolSize);
        stateInfo.setActiveSize(activeCount);
        stateInfo.setCurrentLoad(currentLoad);
        stateInfo.setQueueType(queueType);
        stateInfo.setPeakLoad(peakLoad);
        stateInfo.setQueueSize(queueSize);
        stateInfo.setQueueCapacity(queueCapacity);
        stateInfo.setQueueRemainingCapacity(remainingCapacity);
        stateInfo.setLargestPoolSize(largestPoolSize);
        stateInfo.setCompletedTaskCount(completedTaskCount);

        int rejectCount = pool instanceof DynamicThreadPoolExecutor
                ? ((DynamicThreadPoolExecutor) pool).getRejectCount()
                : -1;
        stateInfo.setRejectCount(rejectCount);
        stateInfo.setClientLastRefreshTime(DateUtil.formatDateTime(new Date()));

        return supplement(stateInfo);
    }

}
