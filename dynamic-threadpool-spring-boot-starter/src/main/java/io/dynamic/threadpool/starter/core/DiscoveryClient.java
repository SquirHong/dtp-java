package io.dynamic.threadpool.starter.core;


import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class DiscoveryClient {

    private final ThreadPoolExecutor heartbeatExecutor;

    private final ScheduledExecutorService scheduler;

    private final HttpAgent httpAgent;

    private volatile long lastSuccessfulHeartbeatTimestamp = -1;

    private static final String PREFIX = "DiscoveryClient_";

    private String appPathIdentifier;

    private final InstanceConfig instanceConfig;

    public DiscoveryClient(HttpAgent httpAgent, InstanceConfig instanceConfig) {
        this.httpAgent = httpAgent;
        this.instanceConfig = instanceConfig;
        heartbeatExecutor = ThreadPoolBuilder.builder()
                .poolThreadSize(1, 5)
                .keepAliveTime(0, TimeUnit.SECONDS)
                .workQueue(new SynchronousQueue())
                .threadFactory("DiscoveryClient-HeartbeatExecutor", true)
                .build();

        scheduler = Executors.newScheduledThreadPool(
                2, ThreadFactoryBuilder.builder().daemon(true).prefix("DiscoveryClient-Scheduler")
                        .build());

        register();

        initScheduledTasks();
    }

    /**
     * 注册实例到服务端
     *
     * @return
     */
    boolean register() {
        log.info("{} {} :: registering service...", PREFIX, appPathIdentifier);

        String urlPath = "/apps/" + appPathIdentifier;

        Result registerResult = null;
        try {
            registerResult = httpAgent.httpPostByDiscovery(urlPath, instanceConfig);
        } catch (Exception ex) {
            log.warn("{} {} - registration failed :: {}.", PREFIX, appPathIdentifier, ex.getMessage(), ex);
            throw ex;
        }

        if (log.isInfoEnabled()) {
            log.info("{} {} - registration status: {}.", PREFIX, appPathIdentifier, registerResult.getCode());
        }

        return registerResult.isSuccess();
    }

    /**
     * 初始化心跳任务
     */
    private void initScheduledTasks() {
        scheduler.schedule(new HeartbeatThread(), 30, TimeUnit.SECONDS);
    }


    /**
     * 与 Server 端保持心跳续约
     */
    public class HeartbeatThread implements Runnable {

        @Override
        public void run() {
            if (renew()) {
                lastSuccessfulHeartbeatTimestamp = System.currentTimeMillis();
            }
        }

    }

    /**
     * 心跳续约
     *
     * @return
     */
    boolean renew() {
        return true;
    }
}
