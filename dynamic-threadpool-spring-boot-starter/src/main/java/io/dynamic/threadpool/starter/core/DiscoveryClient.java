package io.dynamic.threadpool.starter.core;


import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.common.web.exception.ErrorCodeEnum;
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

    private final String appPathIdentifier;

    private final InstanceInfo instanceInfo;

    public DiscoveryClient(HttpAgent httpAgent, InstanceInfo instanceInfo) {
        this.httpAgent = httpAgent;
        this.instanceInfo = instanceInfo;
        this.appPathIdentifier = instanceInfo.getAppName().toUpperCase() + "/" + instanceInfo.getInstanceId();
        this.heartbeatExecutor = ThreadPoolBuilder.builder()
                .poolThreadSize(1, 5)
                .keepAliveTime(0, TimeUnit.SECONDS)
                .workQueue(new SynchronousQueue())
                .threadFactory("DiscoveryClient-HeartbeatExecutor", true)
                .build();

        this.scheduler = Executors.newScheduledThreadPool(
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
        log.info("{}{} - registering service...", PREFIX, appPathIdentifier);

        String urlPath = Constants.BASE_PATH + "/apps/register/";

        Result registerResult = null;
        try {
            registerResult = httpAgent.httpPostByDiscovery(urlPath, instanceInfo);
        } catch (Exception ex) {
            registerResult = Results.failure(ErrorCodeEnum.SERVICE_ERROR);
            log.error("{}{} - registration failed :: {}", PREFIX, appPathIdentifier, ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) {
            log.info("{}{} - registration status :: {}", PREFIX, appPathIdentifier, registerResult.isSuccess() ? "success" : "fail");
        }

        return registerResult.isSuccess();
    }

    /**
     * 初始化心跳任务 30s一次
     */
    private void initScheduledTasks() {
        scheduler.scheduleWithFixedDelay(new HeartbeatThread(), 10, 30, TimeUnit.SECONDS);
    }


    /**
     * 与 Server 端保持心跳续约
     */
    public class HeartbeatThread implements Runnable {

        @Override
        public void run() {
            if (renew()) {
                log.info("客户端实例续约成功");
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
        Result renewResult = null;
        try {
            InstanceInfo.InstanceRenew instanceRenew = new InstanceInfo.InstanceRenew()
                    .setAppName(instanceInfo.getAppName())
                    .setInstanceId(instanceInfo.getInstanceId())
                    .setLastDirtyTimestamp(instanceInfo.getLastDirtyTimestamp().toString())
                    .setStatus(instanceInfo.getStatus().toString());

            renewResult = httpAgent.httpPostByDiscovery(Constants.BASE_PATH + "/apps/renew", instanceRenew);

            if (ErrorCodeEnum.NOT_FOUND.getCode().equals(renewResult.getCode())) {
                long timestamp = instanceInfo.setIsDirtyWithTime();
                boolean success = register();
                if (success) {
                    instanceInfo.unsetIsDirty(timestamp);
                }
                return success;
            }
            return renewResult.isSuccess();
        } catch (Exception ex) {
            log.error(PREFIX + "{} - was unable to send heartbeat!", appPathIdentifier, ex);
            return false;
        }
    }

}
