package io.dynamic.threadpool.starter.alarm;


import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;

public interface SendMessageService {

    /**
     * Send alarm message.
     *
     * @param threadPoolExecutor
     */
    void sendAlarmMessage(CustomThreadPoolExecutor threadPoolExecutor);

    /**
     * Send change message.
     *
     * @param parameter
     */
    void sendChangeMessage(PoolParameterInfo parameter);

}
