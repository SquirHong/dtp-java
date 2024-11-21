package io.dynamic.threadpool.example.test;

import cn.hutool.core.thread.ThreadUtil;
import io.dynamic.threadpool.example.constant.GlobalTestConstant;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TaskDecorator test.
 */
@Slf4j
@Component
public class TaskDecoratorTest {

    public static final String PLACEHOLDER = "site";

    @PostConstruct
    public void taskDecoratorTest() {
        new Thread(() -> {
            MDC.put(PLACEHOLDER, "查看官网: https://www.baidu.com");
            ThreadUtil.sleep(10000);
            DynamicThreadPoolWrapper poolWrapper = GlobalThreadPoolManage.getExecutorService(GlobalTestConstant.MESSAGE_PRODUCE);
            ThreadPoolExecutor threadPoolExecutor = poolWrapper.getExecutor();
            threadPoolExecutor.execute(() -> {
                /**
                 * 此处打印不为空, taskDecorator 即为生效.
                 */
                log.info("通过 taskDecorator MDC 传递上下文 :: {}", MDC.get(PLACEHOLDER));
            });
        }).start();

    }

    public static class ContextCopyingDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            String placeholderVal = MDC.get(PLACEHOLDER);
            return () -> {
                try {
                    MDC.put(PLACEHOLDER, placeholderVal);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }

}
