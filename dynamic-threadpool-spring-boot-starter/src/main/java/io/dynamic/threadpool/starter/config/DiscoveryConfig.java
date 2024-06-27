package io.dynamic.threadpool.starter.config;


import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.starter.core.DiscoveryClient;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.toolkit.CloudCommonIdUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;


@AllArgsConstructor
public class DiscoveryConfig {

    private ConfigurableEnvironment environment;

    @Bean
    @SneakyThrows
    public InstanceInfo instanceInfo() {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setInstanceId(CloudCommonIdUtil.getDefaultInstanceId(environment));
        instanceInfo.setIpApplicationName(CloudCommonIdUtil.getIpApplicationName(environment));
        instanceInfo.setAppName(environment.getProperty("spring.application.name"));
        instanceInfo.setHostName(InetAddress.getLocalHost().getHostAddress());

        return instanceInfo;
    }

    @Bean
    public DiscoveryClient discoveryClient(HttpAgent httpAgent, InstanceInfo instanceInfo) {
        return new DiscoveryClient(httpAgent, instanceInfo);
    }

}
