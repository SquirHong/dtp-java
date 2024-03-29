package io.dynamic.threadpool.starter.toolkit.thread;

import io.dynamic.threadpool.common.toolkit.Assert;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 抽象线程池模版构建
 */
@Slf4j
public class AbstractBuildThreadPoolTemplate {

    /**
     * 线程池构建初始化参数
     *
     * @return
     */
    protected static ThreadPoolInitParam initParam() {
        throw new UnsupportedOperationException();
    }

    /**
     * 构建线程池
     *
     * @return
     */
    public static ThreadPoolExecutor buildPool() {
        ThreadPoolInitParam initParam = initParam();
        return buildPool(initParam);
    }

    /**
     * 构建线程池
     *
     * @return
     */
    public static ThreadPoolExecutor buildPool(ThreadPoolInitParam initParam) {
        Assert.notNull(initParam);
        ThreadPoolExecutor executorService =
                new ThreadPoolExecutorTemplate(initParam.getCorePoolNum(),
                        initParam.getMaxPoolNum(),
                        initParam.getKeepAliveTime(),
                        initParam.getTimeUnit(),
                        initParam.getWorkQueue(),
                        initParam.getThreadFactory(),
                        initParam.rejectedExecutionHandler);
        return executorService;
    }

    /**
     * 构建快速执行线程池
     *
     * @return
     */
    public static ThreadPoolExecutor buildFastPool() {
        ThreadPoolInitParam initParam = initParam();
        return buildFastPool(initParam);
    }

    /**
     * 构建快速执行线程池
     *
     * @return
     */
    public static ThreadPoolExecutor buildFastPool(ThreadPoolInitParam initParam) {
        TaskQueue<Runnable> taskQueue = new TaskQueue(initParam.getCapacity());
        FastThreadPoolExecutor fastThreadPoolExecutor =
                new FastThreadPoolExecutor(initParam.getCorePoolNum(),
                        initParam.getMaxPoolNum(),
                        initParam.getKeepAliveTime(),
                        initParam.getTimeUnit(),
                        taskQueue,
                        initParam.getThreadFactory(),
                        initParam.rejectedExecutionHandler);
        taskQueue.setExecutor(fastThreadPoolExecutor);
        return fastThreadPoolExecutor;
    }

    @Data
    @Accessors(chain = true)
    public static class ThreadPoolInitParam {

        /**
         * 核心线程数量
         */
        private Integer corePoolNum;

        /**
         * 最大线程数量
         */
        private Integer maxPoolNum;

        /**
         * 线程存活时间
         */
        private Long keepAliveTime;

        /**
         * 线程存活时间单位
         */
        private TimeUnit timeUnit;

        /**
         * 队列最大容量
         */
        private Integer capacity;

        /**
         * 阻塞队列
         */
        private BlockingQueue<Runnable> workQueue;

        /**
         * 线程池任务满时拒绝任务策略
         */
        private RejectedExecutionHandler rejectedExecutionHandler;

        /**
         * 创建线程工厂
         */
        private ThreadFactory threadFactory;

        public ThreadPoolInitParam(String threadNamePrefix, boolean isDaemon) {
            this.threadFactory = new ThreadFactoryBuilder()
                    .setNamePrefix(threadNamePrefix + "-")
                    .setDaemon(isDaemon)
                    .build();
        }
    }
}