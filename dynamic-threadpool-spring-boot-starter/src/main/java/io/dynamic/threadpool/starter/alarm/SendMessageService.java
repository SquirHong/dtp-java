package io.dynamic.threadpool.starter.alarm;


import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;

public interface SendMessageService {

    /**
     * Send alarm message.
     *
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(DynamicThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param parameter
     */
    void sendChangeMessage(PoolParameterInfo parameter);

}
