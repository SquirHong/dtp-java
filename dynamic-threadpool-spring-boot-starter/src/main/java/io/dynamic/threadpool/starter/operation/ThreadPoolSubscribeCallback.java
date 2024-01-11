package io.dynamic.threadpool.starter.operation;

/**
 * ThreadPoolSubscribeCallback.
 */
public interface ThreadPoolSubscribeCallback {

    /**
     * 回调函数
     *
     * @param tpId
     * @param config
     */
    void callback(String tpId, String config);
}
