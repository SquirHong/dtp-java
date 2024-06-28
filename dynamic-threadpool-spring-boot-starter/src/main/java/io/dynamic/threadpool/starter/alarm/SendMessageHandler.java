package io.dynamic.threadpool.starter.alarm;



import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;

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
     * @param alarmConfigs
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(List<AlarmConfig> alarmConfigs, CustomThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param alarmConfigs
     * @param parameter
     */
    void sendChangeMessage(List<AlarmConfig> alarmConfigs, PoolParameterInfo parameter);

}
