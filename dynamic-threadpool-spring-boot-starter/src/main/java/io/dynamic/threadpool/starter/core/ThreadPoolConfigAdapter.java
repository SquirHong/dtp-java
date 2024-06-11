package io.dynamic.threadpool.starter.core;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 动态线程池配置适配器
 */
//public class ThreadPoolConfigAdapter extends ConfigAdapter {
//
//    @Resource
//    private ThreadPoolOperation threadPoolOperation;
//
//    /**
//     * 执行修改配置的线程池
//     */
//    private ExecutorService executorService = new ThreadPoolExecutor(
//            2,
//            4,
//            0,
//            TimeUnit.SECONDS,
//            new ArrayBlockingQueue(10),
//            new ThreadFactoryBuilder().setNamePrefix("threadPool-config").build(),
//            new ThreadPoolExecutor.DiscardOldestPolicy());
//
//    @Order(1025)
//    @PostConstruct
//    public void subscribeConfig() {
//        Map<String, DynamicThreadPoolWrap> executorMap =
//                ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);
//
//        List<String> tpIdList = new ArrayList();
//        // 拿到所有线程池id
//        executorMap.forEach((key, val) -> tpIdList.add(val.getTpId()));
//        // 订阅每个配置
//        tpIdList.forEach(each -> threadPoolOperation.subscribeConfig(each, executorService,
//                config -> callbackConfig(config)));
//    }
//
//}
