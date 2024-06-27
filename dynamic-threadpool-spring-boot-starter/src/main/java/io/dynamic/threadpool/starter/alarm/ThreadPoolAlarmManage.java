package io.dynamic.threadpool.starter.alarm;

import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.toolkit.CalculateUtil;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ResizableCapacityLinkedBlockIngQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadPoolAlarmManage {

    public static final SendMessageService SEND_MESSAGE_SERVICE;

    static {
        log.info("ThreadPoolAlarmManage init before");
        SEND_MESSAGE_SERVICE = ApplicationContextHolder.getBean("sendMessageService", SendMessageService.class);
        log.info("ThreadPoolAlarmManage init success");
    }

    /**
     * checkPoolCapacityAlarm.
     *
     * @param threadPoolExecutor
     */
    public static void checkPoolCapacityAlarm(CustomThreadPoolExecutor threadPoolExecutor) {
        ThreadPoolAlarm threadPoolAlarm = threadPoolExecutor.getThreadPoolAlarm();
        ResizableCapacityLinkedBlockIngQueue blockIngQueue = (ResizableCapacityLinkedBlockIngQueue) threadPoolExecutor.getQueue();

        int queueSize = blockIngQueue.size();
        int capacity = queueSize + blockIngQueue.remainingCapacity();
        int divide = CalculateUtil.divide(queueSize, capacity);
        if (divide > threadPoolAlarm.getCapacityAlarm()) {
            log.info("要发送线程池队列容量告警");
            SEND_MESSAGE_SERVICE.sendMessage(threadPoolExecutor);

        }
    }

    /**
     * checkPoolLivenessAlarm.
     *
     * @param isCore
     * @param threadPoolExecutor
     */
    public static void checkPoolLivenessAlarm(boolean isCore, CustomThreadPoolExecutor threadPoolExecutor) {
        log.info("checkPoolLivenessAlarm");
        try {
            if (isCore) {
                return;
            }
            int activeCount = threadPoolExecutor.getActiveCount();
            int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
            int divide = CalculateUtil.divide(activeCount, maximumPoolSize);
            if (divide > threadPoolExecutor.getThreadPoolAlarm().getLivenessAlarm()) {
                log.info("要发送线程池活跃度告警");
                SEND_MESSAGE_SERVICE.sendMessage(threadPoolExecutor);

            }
        } catch (Exception e) {
            log.error("checkPoolLivenessAlarm error", e);

        }
    }

    /**
     * checkPoolRejectAlarm.
     * 也可以在拒绝策略里做
     *
     * @param threadPoolExecutor
     */
    public static void checkPoolRejectAlarm(CustomThreadPoolExecutor threadPoolExecutor) {
        log.info("要发送线程池拒绝告警");
        SEND_MESSAGE_SERVICE.sendMessage(threadPoolExecutor);

    }

}
