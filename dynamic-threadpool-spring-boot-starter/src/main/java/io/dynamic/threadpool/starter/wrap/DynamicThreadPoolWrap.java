package io.dynamic.threadpool.starter.wrap;

import io.dynamic.threadpool.starter.common.CommonThreadPool;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池包装
 */
@Data
public class DynamicThreadPoolWrap {
    private String tenantId;

    private String itemId;

    private String tpId;

    private CustomThreadPoolExecutor pool;

    /**
     * 首选服务端线程池, 为空使用默认线程池 {@link CommonThreadPool#getInstance(String)}
     *
     * @param threadPoolId
     */
    public DynamicThreadPoolWrap(String threadPoolId) {
        this(threadPoolId, CommonThreadPool.getInstance(threadPoolId));
    }

    /**
     *
     * @param threadPoolId
     * @param threadPoolExecutor
     */
    public DynamicThreadPoolWrap(String threadPoolId, CustomThreadPoolExecutor threadPoolExecutor) {
        this.tpId = threadPoolId;
        this.pool = threadPoolExecutor;
    }

    /**
     * 提交任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        pool.execute(command);
    }

    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    /**
     * 提交任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

}
