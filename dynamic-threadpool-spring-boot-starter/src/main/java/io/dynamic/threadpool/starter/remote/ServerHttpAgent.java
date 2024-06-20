package io.dynamic.threadpool.starter.remote;

import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.toolkit.HttpClientUtil;


import java.util.Map;

/**
 * Server Http Agent.
 */
public class ServerHttpAgent implements HttpAgent {

    private final BootstrapProperties dynamicThreadPoolProperties;

    private final ServerListManager serverListManager;

    private final HttpClientUtil httpClientUtil;

    public ServerHttpAgent(BootstrapProperties properties, HttpClientUtil httpClientUtil) {
        this.dynamicThreadPoolProperties = properties;
        this.httpClientUtil = httpClientUtil;
        this.serverListManager = new ServerListManager(dynamicThreadPoolProperties);
    }

    @Override
    public void start() {

    }

    @Override
    public Result httpPostByDiscovery(String path, Object body) {
        return httpClientUtil.restApiPost(buildUrl(path), body, Result.class);
    }

    @Override
    public Result httpGetByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return httpClientUtil.restApiGetByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpPostByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return httpClientUtil.restApiPostByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpDeleteByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return null;
    }

    @Override
    public String getTenantId() {
        return dynamicThreadPoolProperties.getTenantId();
    }

    @Override
    public String getEncode() {
        return null;
    }

    private String buildUrl(String path) {
        return serverListManager.getCurrentServerAddr() + path;
    }
}