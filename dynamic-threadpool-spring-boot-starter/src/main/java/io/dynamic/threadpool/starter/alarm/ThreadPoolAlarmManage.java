package io.dynamic.threadpool.starter.alarm;

import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.config.MessageAlarmConfig;
import io.dynamic.threadpool.starter.toolkit.CalculateUtil;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ResizableCapacityLinkedBlockIngQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class ThreadPoolAlarmManage {

    public static HashMap<String, AlarmControlDTO> currentAlarmMap = new HashMap<>();

    public static final SendMessageService SEND_MESSAGE_SERVICE;

    /**
     * 报警间隔控制
     */
    private static final AlarmControlHandler ALARM_CONTROL_HANDLER;

    static {
        SEND_MESSAGE_SERVICE = Optional.ofNullable(ApplicationContextHolder.getInstance())
                .map(each -> each.getBean(MessageAlarmConfig.SEND_MESSAGE_BEAN_NAME, SendMessageService.class))
                .orElse(null);
        log.info("ThreadPoolAlarmManage init success");
        ALARM_CONTROL_HANDLER = ApplicationContextHolder.getInstance().getBean(AlarmControlHandler.class);
    }

    /**
     * checkPoolCapacityAlarm.
     *
     * @param threadPoolExecutor
     */
    public static void checkPoolCapacityAlarm(DynamicThreadPoolExecutor threadPoolExecutor) {
        if (SEND_MESSAGE_SERVICE == null) {
            return;
        }
        ThreadPoolAlarm threadPoolAlarm = threadPoolExecutor.getThreadPoolAlarm();
        ResizableCapacityLinkedBlockIngQueue blockIngQueue = (ResizableCapacityLinkedBlockIngQueue) threadPoolExecutor.getQueue();

        int queueSize = blockIngQueue.size();
        int capacity = queueSize + blockIngQueue.remainingCapacity();
        int divide = CalculateUtil.divide(queueSize, capacity);
        if ((divide > threadPoolAlarm.getCapacityAlarm()) && isSendMessage(threadPoolExecutor, MessageTypeEnum.CAPACITY)) {
            log.info("要发送线程池队列容量告警");
            SEND_MESSAGE_SERVICE.sendAlarmMessage(threadPoolExecutor);

        }
    }

    /**
     * checkPoolLivenessAlarm.
     *
     * @param isCore
     * @param threadPoolExecutor
     */
    public static void checkPoolLivenessAlarm(boolean isCore, DynamicThreadPoolExecutor threadPoolExecutor) {
        log.info("checkPoolLivenessAlarm");
        try {
            if (isCore || SEND_MESSAGE_SERVICE == null || !isSendMessage(threadPoolExecutor, MessageTypeEnum.LIVENESS)) {
                return;
            }
            int activeCount = threadPoolExecutor.getActiveCount();
            int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
            int divide = CalculateUtil.divide(activeCount, maximumPoolSize);
            if (divide > threadPoolExecutor.getThreadPoolAlarm().getLivenessAlarm()) {
                log.info("要发送线程池活跃度告警");
                SEND_MESSAGE_SERVICE.sendAlarmMessage(threadPoolExecutor);

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
    public static void checkPoolRejectAlarm(DynamicThreadPoolExecutor threadPoolExecutor) {
        if (SEND_MESSAGE_SERVICE == null || !isSendMessage(threadPoolExecutor, MessageTypeEnum.REJECT)) {
            return;
        }
        log.info("要发送线程池拒绝告警");
        SEND_MESSAGE_SERVICE.sendAlarmMessage(threadPoolExecutor);

    }

    /**
     * Send thread pool configuration change message.
     *
     * @param parameter
     */
    public static void sendPoolConfigChange(PoolParameterInfo parameter) {
        if (SEND_MESSAGE_SERVICE == null) {
            return;
        }
        log.info("Send thread pool configuration change message, parameter :: {}", parameter);
        SEND_MESSAGE_SERVICE.sendChangeMessage(parameter);
    }

    private static boolean isSendMessage(DynamicThreadPoolExecutor threadPoolExecutor, MessageTypeEnum typeEnum) {
        AlarmControlDTO alarmControlDTO = currentAlarmMap.computeIfAbsent(
                threadPoolExecutor.getThreadPoolId() + typeEnum,
                key -> AlarmControlDTO.builder()
                        .threadPool(threadPoolExecutor.getThreadPoolId())
                        .typeEnum(typeEnum)
                        .build());
        return ALARM_CONTROL_HANDLER.isSend(alarmControlDTO);
    }

}
