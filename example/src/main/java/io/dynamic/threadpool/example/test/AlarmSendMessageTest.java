package io.dynamic.threadpool.example.test;

import cn.hutool.core.thread.ThreadUtil;
import io.dynamic.threadpool.starter.alarm.ThreadPoolAlarm;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ResizableCapacityLinkedBlockIngQueue;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

import static io.dynamic.threadpool.example.constant.GlobalTestConstant.MESSAGE_PRODUCE;


@Slf4j
@Component
public class AlarmSendMessageTest {

    //    @PostConstruct
    @SuppressWarnings("all")
    public void alarmSendMessageTest() {
        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        scheduledThreadPool.scheduleWithFixedDelay(() -> {
            DynamicThreadPoolWrap executorService = GlobalThreadPoolManage.getExecutorService(MESSAGE_PRODUCE);
            ThreadPoolExecutor poolExecutor = executorService.getPool();
            try {
                poolExecutor.execute(() -> ThreadUtil.sleep(10241024));
            } catch (Exception ex) {
                log.error("抛出拒绝策略", ex.getMessage());
            }
        }, 30, 1, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void test() {
        log.info("开始测试");
        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

        CustomThreadPoolExecutor customThreadPoolExecutor = new CustomThreadPoolExecutor(
                10,
                100,
                0,
                TimeUnit.SECONDS,
                new ResizableCapacityLinkedBlockIngQueue(100),
                "100",
                new CustomizableThreadFactory("test"),
                new ThreadPoolAlarm(true, 2, 3),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.error("拒绝策略");
                    }
                });
        scheduledThreadPool.scheduleWithFixedDelay(() -> {
            log.info("开始执行非spring环境线程池");
            try {
                customThreadPoolExecutor.execute(() ->
                        ThreadUtil.sleep(10241024));
            } catch (Exception ex) {
                log.error("抛出拒绝策略oooooooooo", ex.getMessage());
            }
        }, 10, 1, TimeUnit.SECONDS);
    }
}
