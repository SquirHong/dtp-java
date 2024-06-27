package io.dynamic.threadpool.starter.alarm;


import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;

public interface SendMessageService {

    /**
     * sendMessage.
     *
     * @param threadPoolExecutor
     */
    void sendMessage(CustomThreadPoolExecutor threadPoolExecutor);

}
