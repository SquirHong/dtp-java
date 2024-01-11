package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.starter.core.ThreadPoolRunListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共配置
 */
@Configuration
public class CommonConfiguration {

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

}
