package io.dynamic.threadpool.starter.operation;

import io.dynamic.threadpool.starter.config.DynamicThreadPoolProperties;
import io.dynamic.threadpool.starter.core.ConfigService;
import io.dynamic.threadpool.starter.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * ThreadPoolOperation.
 */
public class ThreadPoolOperation {

    @Resource
    private ConfigService configService;

    private final DynamicThreadPoolProperties properties;

    public ThreadPoolOperation(DynamicThreadPoolProperties properties) {
        this.properties = properties;
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
