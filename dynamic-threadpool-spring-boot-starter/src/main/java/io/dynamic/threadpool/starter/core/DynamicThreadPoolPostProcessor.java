package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.starter.common.CommonDynamicThreadPool;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.task.TaskDecorator;

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
            .poolThreadSize(4, 7)
            .keepAliveTime(0, TimeUnit.MILLISECONDS)
            .workQueue(QueueTypeEnum.ARRAY_BLOCKING_QUEUE, 50)
            .threadFactory("dynamic-threadPool-init-config")
            .allowCoreThreadTimeOut(true)
            .rejected(new ThreadPoolExecutor.DiscardOldestPolicy())
            .build();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof DynamicThreadPoolWrapper) {
            var wrap = (DynamicThreadPoolWrapper) bean;
            log.info("[Init pool] Register thread pool. dynamicThreadPoolWrap :: {}", wrap);
            registerAndSubscribe(wrap);
        } else if (bean instanceof DynamicThreadPoolExecutor) {
            var dynamicThreadPool = ApplicationContextHolder.findAnnotationOnBean(beanName, DynamicThreadPool.class);
            if (Objects.isNull(dynamicThreadPool)) {
                log.info("出现了一个没有加@DynamicThreadPool注解的线程池，将使用默认的线程池配置. beanName :: {}", beanName);
                // 仅仅只是想使用线程池，不需要订阅配置
                return bean;
            }
            var dynamicExecutor = (DynamicThreadPoolExecutor) bean;
            var wrap = new DynamicThreadPoolWrapper(dynamicExecutor.getThreadPoolId(), dynamicExecutor);
            var remoteExecutor = fillPoolAndRegister(wrap);
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
    protected void registerAndSubscribe(DynamicThreadPoolWrapper dynamicThreadPoolWrap) {
        executorService.execute(() -> {
            fillPoolAndRegister(dynamicThreadPoolWrap);
            subscribeConfig(dynamicThreadPoolWrap);
        });
    }

    private ThreadPoolExecutor fillPoolAndRegister(DynamicThreadPoolWrapper dynamicThreadPoolWrap) {
        String tpId = dynamicThreadPoolWrap.getTpId();
        Map<String, String> queryStrMap = new HashMap(3);
        queryStrMap.put(Constants.TP_ID, tpId);
        queryStrMap.put(Constants.ITEM_ID, properties.getItemId());
        queryStrMap.put(Constants.TENANT_ID, properties.getTenantId());

        PoolParameterInfo ppi = new PoolParameterInfo();
        boolean isSubscribe = false;
        Result result = null;
        ThreadPoolExecutor poolExecutor = null;
        try {
            result = httpAgent.httpGetByConfig(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
            log.info("[Init pool] Get thread pool configuration. queryStrMap :: {},result :: {}", queryStrMap, result);
            // 如果数据库有值，则将得到的参数转化为PoolParameterInfo，         没指定的tpid，则使用默认的
            if (result.isSuccess() && result.getData() != null && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
                // 使用相关参数创建线程池
                BlockingQueue workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                RejectedExecutionHandler rejectedExecutionHandler = RejectedTypeEnum.createPolicy(ppi.getRejectedType());
                poolExecutor = ThreadPoolBuilder.builder()
                        .dynamicPool()
                        .poolThreadSize(ppi.getCoreSize(), ppi.getMaxSize())
                        .keepAliveTime(ppi.getKeepAliveTime(), TimeUnit.SECONDS)
                        .workQueue(workQueue)
                        .threadPoolId(tpId)
                        .threadFactory(tpId)
                        .rejected(rejectedExecutionHandler)
                        .alarmConfig(ppi.getIsAlarm(), ppi.getCapacityAlarm(), ppi.getLivenessAlarm())
                        .build();

                if (poolExecutor instanceof DynamicExecutorConfigurationSupport) {
                    TaskDecorator taskDecorator = ((DynamicThreadPoolExecutor) dynamicThreadPoolWrap.getExecutor()).getTaskDecorator();
                    ((DynamicThreadPoolExecutor) poolExecutor).setTaskDecorator(taskDecorator);
                }

                dynamicThreadPoolWrap.setExecutor(poolExecutor);
                isSubscribe = true;
            }
        } catch (Exception ex) {
            log.error("[Init pool] Failed to initialize thread pool configuration. error message :: {},Enhance the default provided thread pool.. ", ex);
            dynamicThreadPoolWrap.setExecutor(CommonDynamicThreadPool.getInstance(tpId));
        } finally {
            // 如果客户端使用了 DynamicThreadPoolExecutor，但没有加@DynamicThreadPool注解，则配置上默认的线程池
            if (Objects.isNull(dynamicThreadPoolWrap.getExecutor())) {
                dynamicThreadPoolWrap.setExecutor(CommonDynamicThreadPool.getInstance(tpId));
            }
            // 设置是否订阅远端线程池配置
            dynamicThreadPoolWrap.setSubscribeFlag(isSubscribe);
        }
        log.info("[Init pool] Thread pool initialization completed. tpId :: {},ppi :: {},dynamicThreadPoolWrap :: {}", tpId, ppi, dynamicThreadPoolWrap);
        GlobalThreadPoolManage.register(tpId, ppi, dynamicThreadPoolWrap);
        return poolExecutor;
    }

    private void subscribeConfig(DynamicThreadPoolWrapper dynamicThreadPoolWrap) {
        log.info("[Init pool] Subscribe thread pool configuration. dynamicThreadPoolWrap :: {}", dynamicThreadPoolWrap);
        // 只有数据库有对应的线程池数据才会去订阅
        if (dynamicThreadPoolWrap.isSubscribeFlag()) {
            threadPoolOperation.subscribeConfig(dynamicThreadPoolWrap.getTpId(), executorService, config -> ThreadPoolDynamicRefresh.refreshDynamicPool(config));
        }
    }
}
