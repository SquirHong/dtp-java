package io.dynamic.threadpool.starter.alarm;


import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;

public interface SendMessageService {

    /**
     * Send alarm message.
     *
     * @param typeEnum
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(MessageTypeEnum typeEnum, DynamicThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param parameter
     */
    void sendChangeMessage(PoolParameterInfo parameter);

}
