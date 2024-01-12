package io.dynamic.threadpool.starter.listener;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.starter.common.CommonThreadPool;
import io.dynamic.threadpool.starter.common.Constants;
import io.dynamic.threadpool.starter.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.config.DynamicThreadPoolProperties;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.remote.ServerHttpAgent;
import io.dynamic.threadpool.starter.toolkit.BlockingQueueUtil;
import io.dynamic.threadpool.starter.toolkit.HttpClientUtil;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池启动监听
 */
public class ThreadPoolRunListener implements ApplicationRunner {

    private final DynamicThreadPoolProperties dynamicThreadPoolProperties;


    public ThreadPoolRunListener(DynamicThreadPoolProperties properties) {
        this.dynamicThreadPoolProperties = properties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, DynamicThreadPoolWrap> executorMap = ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrap.class);

        executorMap.forEach((key, val) -> {

            Map<String, String> queryStrMap = new HashMap();
            queryStrMap.put("tpId", val.getTpId());
            queryStrMap.put("itemId", dynamicThreadPoolProperties.getItemId());
            queryStrMap.put("namespace", dynamicThreadPoolProperties.getNamespace());

            PoolParameterInfo ppi = null;
            HttpAgent httpAgent = new ServerHttpAgent(dynamicThreadPoolProperties);
            Result result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);

            if (result.isSuccess() && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
                // 创建线程池
                TimeUnit unit = TimeUnit.SECONDS;
                BlockingQueue workQueue = BlockingQueueUtil.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
                ThreadPoolExecutor resultTpe = new ThreadPoolExecutor(ppi.getCoreSize(), ppi.getMaxSize(), ppi.getKeepAliveTime(), unit, workQueue);
                val.setPool(resultTpe);
            } else if (val.getPool() == null) {
                val.setPool(CommonThreadPool.getInstance(val.getTpId()));
            }

            GlobalThreadPoolManage.register(val.getTpId(), val);
        });
    }

}
