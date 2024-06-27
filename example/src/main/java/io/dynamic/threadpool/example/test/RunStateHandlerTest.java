package io.dynamic.threadpool.example.test;

import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.dynamic.threadpool.example.test.GlobalTestConstant.TEST_THREAD_POOL_ID;

@Slf4j
@Component
public class RunStateHandlerTest {

//    @PostConstruct
    @SuppressWarnings("all")
    public void runStateHandlerTest() {
        log.info("测试线程池运行时状态接口, 30s 后开始触发拒绝策略...");

        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            DynamicThreadPoolWrap executorService = GlobalThreadPoolManage.getExecutorService(TEST_THREAD_POOL_ID);
            ThreadPoolExecutor pool = executorService.getPool();
            try {
                pool.execute(() -> {
                    log.info("线程池名称 :: {}, 正在执行即将进入阻塞...", Thread.currentThread().getName());
                    try {
                        // 这里为了赋值线程池 completedTaskCount
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                });
            } catch (Exception ex) {
                // ignore
            }

        }, 40000, 100, TimeUnit.MILLISECONDS);
    }
}
