package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class DynamicThreadPoolPostProcessor implements BeanPostProcessor {

    private final BootstrapProperties properties;

    private final HttpAgent httpAgent;

    private final ThreadPoolOperation threadPoolOperation;


    private final ExecutorService executorService = ThreadPoolBuilder.builder()
            .poolThreadSize(2, 4)
            .keepAliveTime(0, TimeUnit.MILLISECONDS)
            .workQueue(QueueTypeEnum.ARRAY_BLOCKING_QUEUE, 5)
            .threadFactory("dynamic-threadPool-config")
            .rejected(new ThreadPoolExecutor.DiscardOldestPolicy())
            .build();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof DynamicThreadPoolWrap) {
            var wrap = (DynamicThreadPoolWrap) bean;
            registerAndSubscribe(wrap);
        } else if (bean instanceof CustomThreadPoolExecutor) {
            var dynamicThreadPool = ApplicationContextHolder.findAnnotationOnBean(beanName, DynamicThreadPool.class);
            if (Objects.isNull(dynamicThreadPool)) {
                return bean;
            }
            var customExecutor = (CustomThreadPoolExecutor) bean;
            var wrap = new DynamicThreadPoolWrap(customExecutor.getThreadPoolId(), customExecutor);
            CustomThreadPoolExecutor remoteExecutor = fillPoolAndRegister(wrap);
            subscribeConfig(wrap);
            return remoteExecutor;
        }

        return bean;
    }

    /**
     * Register and subscribe.
     *
     * @param dynamicThreadPoolWrap
     */
    protected void registerAndSubscribe(DynamicThreadPoolWrap dynamicThreadPoolWrap) {
        fillPoolAndRegister(dynamicThreadPoolWrap);
        subscribeConfig(dynamicThreadPoolWrap);
    }

    private CustomThreadPoolExecutor fillPoolAndRegister(DynamicThreadPoolWrap dynamicThreadPoolWrap) {
        String tpId = dynamicThreadPoolWrap.getTpId();
        Map<String, String> queryStrMap = new HashMap(3);
        queryStrMap.put(Constants.TP_ID, tpId);
        queryStrMap.put(Constants.ITEM_ID, properties.getItemId());
        queryStrMap.put(Constants.TENANT_ID, properties.getTenantId());

        PoolParameterInfo ppi = new PoolParameterInfo();
        boolean isSubscribe = false;
        Result result = null;
        CustomThreadPoolExecutor poolExecutor = null;
        try {
            log.info("[Init pool] Query thread pool configuration from server. ,queryStrMap :: {}", queryStrMap);
            result = httpAgent.httpGetByConfig(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
            // 如果数据库有值，则将得到的参数转化为PoolParameterInfo，         没指定的tpid，则使用默认的
            if (result.isSuccess() && result.getData() != null && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
                // 使用相关参数创建线程池
                BlockingQueue workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                RejectedExecutionHandler rejectedExecutionHandler = RejectedTypeEnum.createPolicy(ppi.getRejectedType());
                poolExecutor = (CustomThreadPoolExecutor) ThreadPoolBuilder.builder()
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
        } finally {
            // 设置是否订阅远端线程池配置
            dynamicThreadPoolWrap.setSubscribeFlag(isSubscribe);
        }
        log.info("[Init pool] Thread pool initialization completed. tpId :: {},ppi :: {},dynamicThreadPoolWrap :: {}", tpId, ppi, dynamicThreadPoolWrap);
        GlobalThreadPoolManage.register(tpId, ppi, dynamicThreadPoolWrap);
        return poolExecutor;
    }

    private void subscribeConfig(DynamicThreadPoolWrap dynamicThreadPoolWrap) {
        // 只有数据库有对应的线程池数据才会去订阅
        if (dynamicThreadPoolWrap.isSubscribeFlag()) {
            threadPoolOperation.subscribeConfig(dynamicThreadPoolWrap.getTpId(), executorService, config -> ThreadPoolDynamicRefresh.refreshDynamicPool(config));
        }
    }
}
