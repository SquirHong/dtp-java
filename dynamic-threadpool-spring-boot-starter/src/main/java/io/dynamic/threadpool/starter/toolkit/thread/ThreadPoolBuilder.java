package io.dynamic.threadpool.starter.toolkit.thread;


import io.dynamic.threadpool.common.toolkit.Assert;
import io.dynamic.threadpool.starter.alarm.ThreadPoolAlarm;
import org.springframework.core.task.TaskDecorator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * 线程池构造器
 */
public class ThreadPoolBuilder implements Builder<ThreadPoolExecutor> {

    /**
     * 是否创建快速消费线程池
     */
    private boolean isFastPool;

    /**
     * 是否动态线程池
     */
    private boolean isDynamicPool;

    /**
     * 核心线程数量
     */
    private Integer corePoolSize = calculateCoreNum();

    /**
     * 最大线程数量
     */
    private Integer maxPoolSize = corePoolSize + (corePoolSize >> 1);

    /**
     * 线程存活时间
     */
    private Integer keepAliveTime = 30000;

    /**
     * 线程存活时间单位
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    /**
     * 队列最大容量
     */
    private Integer capacity = 512;

    /**
     * 队列类型枚举
     */
    private QueueTypeEnum queueType;

    /**
     * 阻塞队列
     */
    private BlockingQueue workQueue = new LinkedBlockingQueue(capacity);

    /**
     * 线程池任务满时拒绝任务策略
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 是否守护线程
     */
    private boolean isDaemon = false;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;

    /**
     * 线程池 ID
     */
    private String threadPoolId;

    /**
     * 是否告警
     */
    private boolean isAlarm = false;

    /**
     * 容量告警
     */
    private Integer capacityAlarm;

    /**
     * 活跃度告警
     */
    private Integer livenessAlarm;

    /**
     * 线程任务装饰器
     */
    private TaskDecorator taskDecorator;

    /**
     * 等待终止毫秒
     */
    private Long awaitTerminationMillis = 5000L;

    /**
     * 等待任务在关机时完成
     */
    private Boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 允许核心线程超时
     */
    private Boolean allowCoreThreadTimeOut = false;

    /**
     * 计算公式：CPU 核数 / (1 - 阻塞系数 0.8)
     *
     * @return 线程池核心线程数
     */
    private Integer calculateCoreNum() {
        int cpuCoreNum = Runtime.getRuntime().availableProcessors();
        return new BigDecimal(cpuCoreNum).divide(new BigDecimal("0.2")).intValue();
    }

    public ThreadPoolBuilder isFastPool(Boolean isFastPool) {
        this.isFastPool = isFastPool;
        return this;
    }

    public ThreadPoolBuilder dynamicPool() {
        this.isDynamicPool = true;
        return this;
    }

    public ThreadPoolBuilder threadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
        return this;
    }

    public ThreadPoolBuilder threadFactory(String threadNamePrefix, Boolean isDaemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.isDaemon = isDaemon;
        return this;
    }

    public ThreadPoolBuilder corePoolNum(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public ThreadPoolBuilder maxPoolNum(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }


    public ThreadPoolBuilder poolThreadSize(Integer corePoolNum, Integer maxPoolNum) {
        this.corePoolSize = corePoolNum;
        this.maxPoolSize = maxPoolNum;
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(Integer keepAliveTime, TimeUnit timeUnit) {
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        return this;
    }

    public ThreadPoolBuilder timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }


    public ThreadPoolBuilder capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public ThreadPoolBuilder workQueue(QueueTypeEnum queueType, Integer capacity) {
        this.queueType = queueType;
        this.capacity = capacity;
        return this;
    }

    public ThreadPoolBuilder rejected(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        return this;
    }

    /**
     * 使用此方式赋值 workQueue, capacity 失效
     *
     * @param queueType
     * @return
     */
    public ThreadPoolBuilder workQueue(QueueTypeEnum queueType) {
        this.queueType = queueType;
        return this;
    }

    public ThreadPoolBuilder workQueue(BlockingQueue workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    public ThreadPoolBuilder threadPoolId(String threadPoolId) {
        this.threadPoolId = threadPoolId;
        return this;
    }

    public ThreadPoolBuilder alarmConfig(int isAlarm, int capacityAlarm, int livenessAlarm) {
        this.isAlarm = isAlarm == 1 ? true : false;
        this.capacityAlarm = capacityAlarm;
        this.livenessAlarm = livenessAlarm;
        return this;
    }

    public ThreadPoolBuilder taskDecorator(TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
        return this;
    }

    public ThreadPoolBuilder awaitTerminationMillis(long awaitTerminationMillis) {
        this.awaitTerminationMillis = awaitTerminationMillis;
        return this;
    }

    public ThreadPoolBuilder waitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        return this;
    }

    public ThreadPoolBuilder dynamicSupport(boolean waitForTasksToCompleteOnShutdown, long awaitTerminationMillis) {
        this.awaitTerminationMillis = awaitTerminationMillis;
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        return this;
    }

    public ThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }

    /**
     * 构建
     *
     * @return
     */
    @Override
    public ThreadPoolExecutor build() {
        if (isDynamicPool) {
            return buildDynamicPool(this);
        }
        return isFastPool ? buildFastPool(this) : buildPool(this);
    }

    /**
     * 创建
     *
     * @return
     */
    public static ThreadPoolBuilder builder() {
        return new ThreadPoolBuilder();
    }

    /**
     * 构建普通线程池
     *
     * @param builder
     * @return
     */
    private static ThreadPoolExecutor buildPool(ThreadPoolBuilder builder) {
        return AbstractBuildThreadPoolTemplate.buildPool(buildInitParam(builder));
    }

    /**
     * 构建快速消费线程池
     *
     * @param builder
     * @return
     */
    private static ThreadPoolExecutor buildFastPool(ThreadPoolBuilder builder) {
        return AbstractBuildThreadPoolTemplate.buildFastPool(buildInitParam(builder));
    }

    /**
     * 构建自定义线程池
     *
     * @param builder
     * @return
     */
    private static ThreadPoolExecutor buildDynamicPool(ThreadPoolBuilder builder) {
        return AbstractBuildThreadPoolTemplate.buildDynamicPool(buildInitParam(builder));
    }

    /**
     * 构建初始化参数
     *
     * @param builder
     * @return
     */
    private static AbstractBuildThreadPoolTemplate.ThreadPoolInitParam buildInitParam(ThreadPoolBuilder builder) {
        Assert.notEmpty(builder.threadNamePrefix, "线程名称前缀不可为空或空的字符串.");
        AbstractBuildThreadPoolTemplate.ThreadPoolInitParam initParam =
                new AbstractBuildThreadPoolTemplate.ThreadPoolInitParam(builder.threadNamePrefix, builder.isDaemon);

        initParam.setCorePoolNum(builder.corePoolSize)
                .setMaxPoolNum(builder.maxPoolSize)
                .setKeepAliveTime(builder.keepAliveTime)
                .setCapacity(builder.capacity)
                .setRejectedExecutionHandler(builder.rejectedExecutionHandler)
                .setTimeUnit(builder.timeUnit)
                .setTaskDecorator(builder.taskDecorator)
                .setAllowCoreThreadTimeOut(builder.allowCoreThreadTimeOut);

        if (builder.isDynamicPool) {
            initParam.setThreadPoolId(Optional.ofNullable(builder.threadPoolId).orElse(builder.threadNamePrefix));
            ThreadPoolAlarm threadPoolAlarm = new ThreadPoolAlarm(builder.isAlarm, builder.livenessAlarm, builder.capacityAlarm);
            initParam.setThreadPoolAlarm(threadPoolAlarm);
            initParam.setWaitForTasksToCompleteOnShutdown(builder.waitForTasksToCompleteOnShutdown);
            initParam.setAwaitTerminationMillis(builder.awaitTerminationMillis);
        }
        // 快速消费线程池内置指定线程池
        if (!builder.isFastPool) {
            if (builder.queueType != null) {
                builder.workQueue = QueueTypeEnum.createBlockingQueue(builder.queueType.type, builder.capacity);
            }
            initParam.setWorkQueue(builder.workQueue);
        }
        System.out.println("hjs------initParam>>>>" + initParam);
        return initParam;
    }
}
