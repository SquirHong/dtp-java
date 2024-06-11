package io.dynamic.threadpool.starter.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态线程池标记配置类
 */
@Configuration(proxyBeanMethods = false)
public class MarkerConfiguration {

    @Bean
    public Marker MarkerBean() {
        return new Marker();
    }

    public class Marker {

    }
}
