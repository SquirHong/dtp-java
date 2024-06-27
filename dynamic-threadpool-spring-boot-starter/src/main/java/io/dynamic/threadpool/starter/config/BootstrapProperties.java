package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.starter.alarm.AlarmConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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

    /**
     * alarms
     */
    private List<AlarmConfig> alarms;

}
