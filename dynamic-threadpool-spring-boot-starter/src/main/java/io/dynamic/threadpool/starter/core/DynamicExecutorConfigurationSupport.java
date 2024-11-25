package io.dynamic.threadpool.starter.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;

@Slf4j
public abstract class DynamicExecutorConfigurationSupport extends ThreadPoolExecutor implements InitializingBean, DisposableBean {

    private String threadPoolId;

    private ExecutorService executor;

    private long awaitTerminationMillis = 0;

    private boolean waitForTasksToCompleteOnShutdown = false;

    public DynamicExecutorConfigurationSupport(int corePoolSize,
                                               int maximumPoolSize,
                                               long keepAliveTime,
                                               TimeUnit unit,
                                               boolean waitForTasksToCompleteOnShutdown,
                                               long awaitTerminationMillis,
                                               BlockingQueue<Runnable> workQueue,
                                               String threadPoolId,
                                               ThreadFactory threadFactory,
                                               RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.threadPoolId = threadPoolId;
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        this.awaitTerminationMillis = awaitTerminationMillis;
    }

    /**
     * Create the target {@link java.util.concurrent.ExecutorService} instance.
     * Called by {@code afterPropertiesSet}.
     *
     * @return a new ExecutorService instance
     * @see #afterPropertiesSet()
     */
    protected abstract ExecutorService initializeExecutor();

    /**
     * Calls {@code initialize()} after the container applied all property values.
     *
     * @see #initialize()
     */
    @Override
    public void afterPropertiesSet() {
        initialize();
    }

    /**
     * Calls {@code shutdown} when the BeanFactory destroys.
     * the task executor instance.
     *
     * @see #shutdown()
     */
    @Override
    public void destroy() {
        shutdownSupport();
    }

    /**
     * Set up the ExecutorService.
     */
    public void initialize() {
        if (log.isInfoEnabled()) {
            log.info("Initializing ExecutorService" + (this.threadPoolId != null ? " '" + this.threadPoolId + "'" : ""));
        }

        this.executor = initializeExecutor();
    }

    /**
     * Perform a shutdown on the underlying ExecutorService.
     *
     * @see java.util.concurrent.ExecutorService#shutdown()
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    public void shutdownSupport() {
        if (log.isInfoEnabled()) {
            log.info("Shutting down ExecutorService" + (this.threadPoolId != null ? " '" + this.threadPoolId + "'" : ""));
        }
        if (this.executor != null) {
            if (this.waitForTasksToCompleteOnShutdown) {
                this.executor.shutdown();
            } else {
                for (Runnable remainingTask : this.executor.shutdownNow()) {
                    cancelRemainingTask(remainingTask);
                }
            }
            awaitTerminationIfNecessary(this.executor);
        }
    }

    protected void cancelRemainingTask(Runnable task) {
        if (task instanceof Future) {
            ((Future<?>) task).cancel(true);
        }
    }

    private void awaitTerminationIfNecessary(ExecutorService executor) {
        if (this.awaitTerminationMillis > 0) {
            try {
                if (!executor.awaitTermination(this.awaitTerminationMillis, TimeUnit.MILLISECONDS)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Timed out while waiting for executor" + (this.threadPoolId != null ? " '" + this.threadPoolId + "'" : "") + " to terminate");
                    }
                }
            } catch (InterruptedException ex) {
                if (log.isWarnEnabled()) {
                    log.warn("Interrupted while waiting for executor" + (this.threadPoolId != null ? " '" + this.threadPoolId + "'" : "") + " to terminate");
                }
                Thread.currentThread().interrupt();
            }
        }
    }

}
