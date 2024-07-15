package io.dynamic.threadpool.starter.wrap;

import io.dynamic.threadpool.starter.common.CommonDynamicThreadPool;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池包装
 */
@Data
public class DynamicThreadPoolWrapper {
    private String tenantId;

    private String itemId;

    private String tpId;

    private boolean subscribeFlag;

    private DynamicThreadPoolExecutor pool;

    /**
     * 首选服务端线程池, 为空使用默认线程池 {@link CommonDynamicThreadPool#getInstance(String)}
     *
     * @param threadPoolId
     */
    public DynamicThreadPoolWrapper(String threadPoolId) {
        this(threadPoolId, CommonDynamicThreadPool.getInstance(threadPoolId));
    }

    /**
     *
     * @param threadPoolId
     * @param threadPoolExecutor
     */
    public DynamicThreadPoolWrapper(String threadPoolId, DynamicThreadPoolExecutor threadPoolExecutor) {
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
