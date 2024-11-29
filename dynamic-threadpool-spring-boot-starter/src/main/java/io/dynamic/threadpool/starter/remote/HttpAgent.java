package io.dynamic.threadpool.starter.remote;


import io.dynamic.threadpool.common.web.base.Result;

import java.util.Map;

public interface HttpAgent {

    /**
     * 开始获取服务集合
     */
    void start();

    /**
     * 获取租户id
     *
     * @return
     */
    String getTenantId();

    /**
     * 获取编码集
     *
     * @return
     */
    String getEncode();

    /**
     * Http post.
     *
     * @param path
     * @param body
     * @return
     */
    Result httpPost(String path, Object body);

    /**
     * @param path
     * @param body
     * @return
     */
    Result httpPostByDiscovery(String path, Object body);

    /**
     * @param path
     * @param headers
     * @param paramValues
     * @param readTimeoutMs
     * @return
     */
    Result httpGetByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs);

    /**
     * @param path
     * @param headers
     * @param paramValues
     * @param readTimeoutMs
     * @return
     */
    Result httpPostByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs);

    /**
     * @param path
     * @param headers
     * @param paramValues
     * @param readTimeoutMs
     * @return
     */
    Result httpDeleteByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs);
}
