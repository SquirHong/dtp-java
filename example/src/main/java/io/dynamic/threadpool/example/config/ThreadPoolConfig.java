package io.dynamic.threadpool.example.config;

import io.dynamic.threadpool.starter.core.DynamicThreadPool;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadPoolBuilder;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.*;

import static io.dynamic.threadpool.example.constant.GlobalTestConstant.MESSAGE_CONSUME;
import static io.dynamic.threadpool.example.constant.GlobalTestConstant.MESSAGE_PRODUCE;

/**
 * 线程池配置
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * {@link DynamicThreadPoolWrap} 完成 Server 端订阅配置功能.
     *
     * @return
     */
    @Bean
    public DynamicThreadPoolWrap messageCenterConsumeThreadPool() {
        return new DynamicThreadPoolWrap(MESSAGE_CONSUME);
    }

    /**
     * 通过 {@link DynamicThreadPool} 修饰 {@link CustomThreadPoolExecutor} 完成 Server 端订阅配置功能.
     * <p>
     * 由动态线程池注解修饰后, IOC 容器中保存的是 {@link CustomThreadPoolExecutor}
     *
     * @return
     */
    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor customThreadPoolExecutor() {
        return ThreadPoolBuilder.builder().threadFactory(MESSAGE_PRODUCE).threadPoolId(MESSAGE_PRODUCE).isCustomPool(true).build();
    }

    @Bean
    public DynamicThreadPoolWrap MMMMMMMMThreadPool() {
        return new DynamicThreadPoolWrap("MMMMMMMM");
    }

}
