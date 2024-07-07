package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.controller.PoolRunStateController;
import io.dynamic.threadpool.starter.core.ConfigService;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolPostProcessor;

import io.dynamic.threadpool.starter.core.ThreadPoolConfigService;

import io.dynamic.threadpool.starter.core.ThreadPoolOperation;
import io.dynamic.threadpool.starter.enable.MarkerConfiguration;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 动态线程池自动装配类
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(BootstrapProperties.class)
// 动态线程池启用的开关
@ConditionalOnBean(MarkerConfiguration.Marker.class)
@ImportAutoConfiguration({HttpClientConfig.class, DiscoveryConfig.class, MessageAlarmConfig.class})
public class DynamicThreadPoolAutoConfiguration {

    private final BootstrapProperties properties;

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ConfigService configService(HttpAgent httpAgent) {
        return new ThreadPoolConfigService(httpAgent);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ThreadPoolOperation threadPoolOperation(ConfigService configService) {
        return new ThreadPoolOperation(properties, configService);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public DynamicThreadPoolPostProcessor threadPoolBeanPostProcessor(HttpAgent httpAgent, ThreadPoolOperation threadPoolOperation,
                                                                      ApplicationContextHolder applicationContextHolder) {
        return new DynamicThreadPoolPostProcessor(properties, httpAgent, threadPoolOperation);
    }

    @Bean
    public PoolRunStateController poolRunStateController() {
        return new PoolRunStateController();
    }
}
