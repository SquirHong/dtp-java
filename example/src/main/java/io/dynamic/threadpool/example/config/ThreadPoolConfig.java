package io.dynamic.threadpool.example.config;

import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 线程池配置
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public DynamicThreadPoolWrap messageCenterConsumeThreadPool() {
        return new DynamicThreadPoolWrap("message-consume");
    }

}
