package io.dynamic.threadpool.starter.alarm;


import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;

public interface SendMessageHandler {

    /**
     * getType.
     *
     * @return
     */
    String getType();

    /**
     * Send alarm message.
     *
     * @param notifyConfig
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(NotifyDTO notifyConfig, DynamicThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param notifyConfig
     * @param parameter
     */
    void sendChangeMessage(NotifyDTO notifyConfig, PoolParameterInfo parameter);

}
