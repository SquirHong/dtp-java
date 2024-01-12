package io.dynamic.threadpool.starter.adapter;


import io.dynamic.threadpool.starter.core.ThreadPoolDynamicRefresh;

/**
 *  配置处理器
 */
public class ConfigAdapter {

    /**
     * 回调修改线程池配置
     *
     * @param config
     */
    public void callbackConfig(String config) {
        ThreadPoolDynamicRefresh.refreshDynamicPool(config);
    }
}
