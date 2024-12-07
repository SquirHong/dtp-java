package io.dynamic.threadpool.example.test;

import cn.hutool.core.thread.ThreadUtil;
import io.dynamic.threadpool.example.constant.GlobalTestConstant;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class RunStateHandlerTest {

//    @PostConstruct
    @SuppressWarnings("all")
    public void runStateHandlerTest() {
        log.info("测试线程池运行时状态接口, 30s 后开始触发拒绝策略...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("开始zhixing...");
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                DynamicThreadPoolWrapper poolWrapper = GlobalThreadPoolManage.getExecutorService(GlobalTestConstant.MESSAGE_PRODUCE);
                ThreadPoolExecutor pool = poolWrapper.getExecutor();
                try {
                    pool.execute(() -> {
                        log.info("Thread pool name :: {}, Executing incoming blocking...", Thread.currentThread().getName());
                        try {
                            int maxRandom = 10;
                            int temp = 2;
                            Random random = new Random();
                            // Assignment thread pool completedTaskCount
                            if (random.nextInt(maxRandom) % temp == 0) {
                                Thread.sleep(10241024);
                            } else {
                                Thread.sleep(5000);
                            }
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    });
                } catch (Exception ex) {
                    // ignore
                }

                log.info("  >>> Number of dynamic thread pool tasks executed :: {}", i);
                ThreadUtil.sleep(150);
            }

        }).start();
    }
}
