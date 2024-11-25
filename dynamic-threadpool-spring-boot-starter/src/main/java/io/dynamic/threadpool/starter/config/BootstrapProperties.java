package io.dynamic.threadpool.starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 动态线程池配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = BootstrapProperties.PREFIX)
public class BootstrapProperties {

    public static final String PREFIX = "spring.dynamic.thread-pool";

    /**
     * 服务地址
     */
    private String serverAddr;

    /**
     * 命名空间
     */
    private String tenantId;

    /**
     * 项目 Id
     */
    private String itemId;

}
