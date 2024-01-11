package io.dynamic.threadpool.starter.core;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.starter.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  最底层真正修改线程池参数的工具
 */
public class ThreadPoolDynamicRefresh {

    public static void refreshDynamicPool(String tpId, String content) {
        PoolParameterInfo parameter = JSON.parseObject(content, PoolParameterInfo.class);
        refreshDynamicPool(tpId, parameter.getCoreSize(), parameter.getMaxSize(), parameter.getCapacity(), parameter.getKeepAliveTime());
    }

    public static void refreshDynamicPool(String threadPoolId, Integer coreSize, Integer maxSize, Integer capacity, Integer keepAliveTime) {
        DynamicThreadPoolWrap wrap = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        ThreadPoolExecutor executor = wrap.getPool();
        // TODO: 2024/1/10  这里设置大小，有个先后问题，后期可以引入更多的线程池类型，还可以是forkjoinpool、StandardThreadExecutor
        if (coreSize != null) {
            executor.setCorePoolSize(coreSize);
        }
        if (maxSize != null) {
            executor.setMaximumPoolSize(maxSize);
        }
        if (capacity != null) {
            ResizableCapacityLinkedBlockIngQueue queue = (ResizableCapacityLinkedBlockIngQueue) executor.getQueue();
            queue.setCapacity(capacity);
        }
        if (keepAliveTime != null) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }
    }

}
