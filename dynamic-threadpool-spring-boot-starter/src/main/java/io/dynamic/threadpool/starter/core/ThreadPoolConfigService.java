package io.dynamic.threadpool.starter.core;


import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.remote.ServerHttpAgent;

import java.util.Arrays;

/**
 * 线程池配置服务
 */
public class ThreadPoolConfigService implements ConfigService {

    private final ClientWorker clientWorker;

    private final HttpAgent httpAgent;

    public ThreadPoolConfigService(HttpAgent httpAgent) {
        this.httpAgent = httpAgent;
        clientWorker = new ClientWorker(httpAgent);
    }

    @Override
    public void addListener(String tenantId, String itemId, String tpId, Listener listener) {
        clientWorker.addTenantListeners(tenantId, itemId, tpId, Arrays.asList(listener));
    }

    @Override
    public String getServerStatus() {
        if (clientWorker.isHealthServer()) {
            return "UP";
        } else {
            return "DOWN";
        }
    }
}