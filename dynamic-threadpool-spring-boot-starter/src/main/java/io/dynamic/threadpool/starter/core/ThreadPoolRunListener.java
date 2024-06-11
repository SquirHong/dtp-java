package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.remote.ServerHttpAgent;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池启动监听
 */
//@Slf4j
//public class ThreadPoolRunListener {
//
//    private final DynamicThreadPoolProperties properties;
//
//
//    public ThreadPoolRunListener(DynamicThreadPoolProperties properties) {
//        this.properties = properties;
//    }
//
//    @Order(1024)
//    @PostConstruct
//    public void run() {
//        Map<String, DynamicThreadPoolWrap> executorMap = ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);
//
//        executorMap.forEach((key, val) -> {
//
//            Map<String, String> queryStrMap = new HashMap();
//            queryStrMap.put("tpId", val.getTpId());
//            queryStrMap.put("itemId", properties.getItemId());
//            queryStrMap.put("tenantId", properties.getTenantId());
//
//            PoolParameterInfo ppi = new PoolParameterInfo();
//            HttpAgent httpAgent = new ServerHttpAgent(properties);
//            Result result = null;
//            try {
//                result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
//                // 如果数据库有值，则将得到的参数转化为PoolParameterInfo，         没指定的tpid，则使用默认的
//                if (result.isSuccess() && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
//                    // 使用相关参数创建线程池
//                    BlockingQueue workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
//                    RejectedExecutionHandler rejectedExecutionHandler = RejectedTypeEnum.createPolicy(ppi.getRejectedType());
//                    ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
//                            .isCustomPool(true)
//                            .poolThreadSize(ppi.getCoreSize(), ppi.getMaxSize())
//                            .keepAliveTime(ppi.getKeepAliveTime(), TimeUnit.SECONDS)
//                            .workQueue(workQueue)
//                            .threadFactory(val.getTpId())
//                            .rejected(rejectedExecutionHandler)
//                            .build();
//                    val.setPool(poolExecutor);
//                } else if (val.getPool() == null) {
//                    val.setPool(CommonThreadPool.getInstance(val.getTpId()));
//                }
//            } catch (Exception ex) {
//                log.error("[Init pool] Failed to initialize thread pool configuration. error message :: {}", ex.getMessage());
//                val.setPool(CommonThreadPool.getInstance(val.getTpId()));
//            }
//
//            GlobalThreadPoolManage.register(val.getTpId(), ppi, val);
//            log.info("[Init pool] Thread pool initialization completed. tpId :: {},ppi :: {}", val.getTpId(),ppi);
//        });
//    }
//
//}
