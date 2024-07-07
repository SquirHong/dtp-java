package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.starter.common.CommonThreadPool;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class DynamicThreadPoolPostProcessor implements BeanPostProcessor {

    private final BootstrapProperties properties;

    private final ThreadPoolOperation threadPoolOperation;

    private final HttpAgent httpAgent;

    public DynamicThreadPoolPostProcessor(BootstrapProperties properties, HttpAgent httpAgent,
                                          ThreadPoolOperation threadPoolOperation) {
        this.properties = properties;
        this.httpAgent = httpAgent;
        this.threadPoolOperation = threadPoolOperation;
    }

    private final ExecutorService executorService = ThreadPoolBuilder.builder()
            .poolThreadSize(2, 4)
            .keepAliveTime(0, TimeUnit.MILLISECONDS)
            .workQueue(QueueTypeEnum.ARRAY_BLOCKING_QUEUE, 5)
            .threadFactory("dynamic-threadPool-config")
            .rejected(new ThreadPoolExecutor.DiscardOldestPolicy())
            .build();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!(bean instanceof DynamicThreadPoolWrap)) {
            return bean;
        }
        DynamicThreadPoolWrap dynamicThreadPoolWrap = (DynamicThreadPoolWrap) bean;
        log.info("[Init pool] Start to initialize thread pool configuration. tpId :: {}", dynamicThreadPoolWrap.getTpId());
        // 根据 TpId 向 Server 端发起请求，查询是否有远程配置
        fillPoolAndRegister(dynamicThreadPoolWrap);
        // 订阅 Server 端配置
        subscribeConfig(dynamicThreadPoolWrap);

        return bean;
    }

    private void fillPoolAndRegister(DynamicThreadPoolWrap dynamicThreadPoolWrap) {
        String tpId = dynamicThreadPoolWrap.getTpId();
        Map<String, String> queryStrMap = new HashMap(3);
        queryStrMap.put("tpId", tpId);
        queryStrMap.put("itemId", properties.getItemId());
        queryStrMap.put("tenantId", properties.getTenantId());

        PoolParameterInfo ppi = new PoolParameterInfo();
        boolean isSubscribe = false;
        Result result = null;

        try {
            log.info("[Init pool] Query thread pool configuration from server. ,queryStrMap :: {}", queryStrMap);
            result = httpAgent.httpGetByConfig(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
            // 如果数据库有值，则将得到的参数转化为PoolParameterInfo，         没指定的tpid，则使用默认的
            if (result.isSuccess() && result.getData() != null && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
                // 使用相关参数创建线程池
                BlockingQueue workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                RejectedExecutionHandler rejectedExecutionHandler = RejectedTypeEnum.createPolicy(ppi.getRejectedType());
                CustomThreadPoolExecutor poolExecutor = (CustomThreadPoolExecutor) ThreadPoolBuilder.builder()
                        .isCustomPool(true)
                        .poolThreadSize(ppi.getCoreSize(), ppi.getMaxSize())
                        .keepAliveTime(ppi.getKeepAliveTime(), TimeUnit.SECONDS)
                        .workQueue(workQueue)
                        .threadPoolId(tpId)
                        .threadFactory(tpId)
                        .rejected(rejectedExecutionHandler)
                        .alarmConfig(ppi.getIsAlarm(), ppi.getCapacityAlarm(), ppi.getLivenessAlarm())
                        .build();
                dynamicThreadPoolWrap.setPool(poolExecutor);
                isSubscribe = true;
            }
        } catch (Exception ex) {
            log.error("[Init pool] Failed to initialize thread pool configuration. error message :: {},Enhance the default provided thread pool.. ", ex.getMessage());
            dynamicThreadPoolWrap.setPool(CommonThreadPool.getInstance(tpId));
        }finally {
            // 设置是否订阅远端线程池配置
            dynamicThreadPoolWrap.setSubscribeFlag(isSubscribe);
        }
        log.info("[Init pool] Thread pool initialization completed. tpId :: {},ppi :: {},dynamicThreadPoolWrap :: {}", tpId, ppi, dynamicThreadPoolWrap);
        GlobalThreadPoolManage.register(tpId, ppi, dynamicThreadPoolWrap);
    }

    private void subscribeConfig(DynamicThreadPoolWrap dynamicThreadPoolWrap) {
        // 只有数据库有对应的线程池数据才会去订阅
        if (dynamicThreadPoolWrap.isSubscribeFlag()) {
            threadPoolOperation.subscribeConfig(dynamicThreadPoolWrap.getTpId(), executorService, config -> ThreadPoolDynamicRefresh.refreshDynamicPool(config));
        }
    }
}
