package io.dynamic.threadpool.starter.wrap;

import io.dynamic.threadpool.starter.common.CommonDynamicThreadPool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池包装
 */
@Data
@Slf4j
public class DynamicThreadPoolWrapper {
    private String tenantId;

    private String itemId;

    private String tpId;

    private boolean subscribeFlag;

    private ThreadPoolExecutor executor;

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
    public DynamicThreadPoolWrapper(String threadPoolId, ThreadPoolExecutor threadPoolExecutor) {
        this.tpId = threadPoolId;
        this.executor = threadPoolExecutor;
    }

    /**
     * 提交任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        executor.execute(command);
    }

    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    /**
     * 提交任务
     *
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

}
