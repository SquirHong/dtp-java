package io.dynamic.threadpool.starter.alarm;



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
     * sendMessage.
     *
     * @param alarmConfigs
     * @param threadPoolExecutor
     */
    void sendMessage(List<AlarmConfig> alarmConfigs, CustomThreadPoolExecutor threadPoolExecutor);

}
