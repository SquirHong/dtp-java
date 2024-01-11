package io.dynamic.threadpool.starter.listener;

import java.util.concurrent.Executor;

/**
 * 监听器
 */
public interface Listener {

    /**
     * 获取执行器
     *
     * @return
     */
    Executor getExecutor();

    /**
     * 接受配置信息
     *
     * @param configInfo
     */
    void receiveConfigInfo(String configInfo);
}
