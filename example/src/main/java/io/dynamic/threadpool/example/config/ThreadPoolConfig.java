package io.dynamic.threadpool.example.config;

import io.dynamic.threadpool.starter.core.DynamicThreadPool;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

import static io.dynamic.threadpool.example.constant.GlobalTestConstant.MESSAGE_PRODUCE;

/**
 * 线程池配置
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * {@link DynamicThreadPoolWrapper} 完成 Server 端订阅配置功能.
     *
     * @return
     */
//    @Bean
//    public DynamicThreadPoolWrapper messageCenterConsumeThreadPool() {
//        ThreadPoolExecutor customExecutor = ThreadPoolBuilder.builder()
//                .dynamicPool()
//                .threadFactory(MESSAGE_CONSUME)
//                .build();
//        return new DynamicThreadPoolWrapper(MESSAGE_CONSUME, customExecutor);
//    }

    /**
     * 通过 {@link DynamicThreadPool} 修饰 {@link DynamicThreadPoolExecutor} 完成 Server 端订阅配置功能.
     * <p>
     * 由动态线程池注解修饰后, IOC 容器中保存的是 {@link DynamicThreadPoolExecutor}
     *
     * @return
     */
    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor customThreadPoolExecutor() {
        return ThreadPoolBuilder.builder().threadFactory(MESSAGE_PRODUCE).threadPoolId(MESSAGE_PRODUCE).dynamicPool().build();
    }

//    @Bean
//    public DynamicThreadPoolWrapper MMMMMMMMThreadPool() {
//        return new DynamicThreadPoolWrapper("MMMMMMMM");
//    }

}
