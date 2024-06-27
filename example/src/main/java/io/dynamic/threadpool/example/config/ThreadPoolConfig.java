package io.dynamic.threadpool.example.config;

import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 线程池配置
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    private String messageConsumePrefix = "message-consume";

    private String messageProducePrefix = "message-produce";

    private String customPoolPrefix = "custom-pool";

    @Bean
    public DynamicThreadPoolWrap messageCenterConsumeThreadPool() {
        return new DynamicThreadPoolWrap(messageConsumePrefix);
    }

    @Bean
    public DynamicThreadPoolWrap messageCenterProduceThreadPool() {
        return new DynamicThreadPoolWrap(messageProducePrefix);
    }

    @Bean
    public DynamicThreadPoolWrap messageCenterhjsThreadPool() {
        return new DynamicThreadPoolWrap("hjs-pool");
    }

    @Bean
    public DynamicThreadPoolWrap customPool() {
        return new DynamicThreadPoolWrap(customPoolPrefix);
    }


}
