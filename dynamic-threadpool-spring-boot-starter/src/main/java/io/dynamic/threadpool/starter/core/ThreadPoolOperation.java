package io.dynamic.threadpool.starter.core;

import io.dynamic.threadpool.starter.config.BootstrapProperties;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * ThreadPoolOperation.
 */
public class ThreadPoolOperation {

    private final ConfigService configService;

    private final BootstrapProperties properties;

    public ThreadPoolOperation(BootstrapProperties properties, ConfigService configService) {
        this.properties = properties;
        this.configService = configService;
    }

    public Listener subscribeConfig(String tpId, Executor executor, ThreadPoolSubscribeCallback threadPoolSubscribeCallback) {
        Listener configListener = new Listener() {
            @Override
            public Executor getExecutor() {
                return executor;
            }

            @Override
            public void receiveConfigInfo(String config) {
                threadPoolSubscribeCallback.callback(config);
            }
        };

        configService.addListener(properties.getTenantId(), properties.getItemId(), tpId, configListener);

        return configListener;
    }
}
