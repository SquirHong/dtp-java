package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.starter.toolkit.InetUtils;
import io.dynamic.threadpool.starter.toolkit.InetUtilsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Util auto configuration.
 */
@EnableConfigurationProperties(InetUtilsProperties.class)
public class UtilAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InetUtils inetUtils(InetUtilsProperties properties) {
        return new InetUtils(properties);
    }

}
