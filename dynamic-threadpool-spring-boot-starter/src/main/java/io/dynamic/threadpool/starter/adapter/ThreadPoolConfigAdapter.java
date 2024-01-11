package io.dynamic.threadpool.starter.adapter;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.starter.operation.ThreadPoolOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  动态线程池配置适配器
 */
public class ThreadPoolConfigAdapter extends ConfigAdapter {

    @Resource
    private ThreadPoolOperation threadPoolOperation;

    /**
     *  修改配置的线程池
     */
    private ExecutorService executorService = new ThreadPoolExecutor(
            2,
            4,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue(5),
            new ThreadFactoryBuilder().setNamePrefix("threadPool-config").build(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public void subscribeConfig(List<String> tpIds) {
        tpIds.forEach(each -> threadPoolOperation.subscribeConfig(each, executorService, (tpId, config) -> callbackConfig(tpId, config)));
    }

}
