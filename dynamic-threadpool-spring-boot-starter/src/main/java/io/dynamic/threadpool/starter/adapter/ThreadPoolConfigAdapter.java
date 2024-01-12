package io.dynamic.threadpool.starter.adapter;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.starter.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.operation.ThreadPoolOperation;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.beans.factory.annotation.Autowired;

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
public class ThreadPoolConfigAdapter extends ConfigAdapter {

    @Resource
    private ThreadPoolOperation threadPoolOperation;

    /**
     * 修改配置的线程池
     */
    private ExecutorService executorService = new ThreadPoolExecutor(
            2,
            4,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue(5),
            new ThreadFactoryBuilder().setNamePrefix("threadPool-config").build(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    @PostConstruct
    public void subscribeConfig() {
        Map<String, DynamicThreadPoolWrap> executorMap =
                ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);

        List<String> tpIdList = new ArrayList();
        executorMap.forEach((key, val) -> tpIdList.add(val.getTpId()));

        tpIdList.forEach(each -> threadPoolOperation.subscribeConfig(each, executorService,
                config -> callbackConfig(config)));
    }

}
