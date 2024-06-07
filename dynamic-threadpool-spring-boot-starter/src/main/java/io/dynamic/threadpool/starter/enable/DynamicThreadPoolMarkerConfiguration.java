package io.dynamic.threadpool.starter.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态线程池标记配置类
 */
@Configuration(proxyBeanMethods = false)
public class DynamicThreadPoolMarkerConfiguration {

    @Bean
    public Marker dynamicThreadPoolMarkerBean() {
        return new Marker();
    }

    public class Marker {

    }
}
