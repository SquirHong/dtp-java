package io.dynamic.threadpool.example.test;

import cn.hutool.core.thread.ThreadUtil;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.dynamic.threadpool.example.constant.GlobalTestConstant.MESSAGE_PRODUCE;


@Slf4j
@Component
public class AlarmSendMessageTest {

    //    @PostConstruct
    @SuppressWarnings("all")
    public void alarmSendMessageTest() {
        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        scheduledThreadPool.scheduleWithFixedDelay(() -> {
            DynamicThreadPoolWrapper executorService = GlobalThreadPoolManage.getExecutorService(MESSAGE_PRODUCE);
            ThreadPoolExecutor poolExecutor = executorService.getExecutor();
            try {
                poolExecutor.execute(() -> ThreadUtil.sleep(10241024));
            } catch (Exception ex) {
                log.error("抛出拒绝策略", ex.getMessage());
            }
        }, 30, 1, TimeUnit.SECONDS);
    }

//    @PostConstruct
//    public void test() {
//        log.info("开始测试");
//        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
//
//        DynamicThreadPoolExecutor customThreadPoolExecutor = new DynamicThreadPoolExecutor(
//                10,
//                100,
//                0,
//                TimeUnit.SECONDS,
//                new ResizableCapacityLinkedBlockIngQueue(100),
//                "100",
//                new CustomizableThreadFactory("test"),
//                new ThreadPoolAlarm(true, 2, 3),
//                new RejectedExecutionHandler() {
//                    @Override
//                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                        log.error("拒绝策略");
//                    }
//                });
//        scheduledThreadPool.scheduleWithFixedDelay(() -> {
//            log.info("开始执行非spring环境线程池");
//            try {
//                customThreadPoolExecutor.execute(() ->
//                        ThreadUtil.sleep(10241024));
//            } catch (Exception ex) {
//                log.error("抛出拒绝策略oooooooooo", ex.getMessage());
//            }
//        }, 10, 1, TimeUnit.SECONDS);
//    }
}
