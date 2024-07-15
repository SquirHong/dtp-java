package io.dynamic.threadpool.starter.alarm;



import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;

import java.util.List;

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
     * @param notifyConfigs
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(List<NotifyConfig> notifyConfigs, DynamicThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param notifyConfigs
     * @param parameter
     */
    void sendChangeMessage(List<NotifyConfig> notifyConfigs, PoolParameterInfo parameter);

}
