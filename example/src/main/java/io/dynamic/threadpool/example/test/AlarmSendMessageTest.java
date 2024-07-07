package io.dynamic.threadpool.example.test;

import cn.hutool.core.thread.ThreadUtil;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
            DynamicThreadPoolWrap executorService = GlobalThreadPoolManage.getExecutorService(MESSAGE_PRODUCE);
            ThreadPoolExecutor poolExecutor = executorService.getPool();
            try {
                poolExecutor.execute(() -> ThreadUtil.sleep(10241024));
            } catch (Exception ex) {
                log.error("抛出拒绝策略", ex.getMessage());
            }
        }, 30, 1, TimeUnit.SECONDS);
    }

}
