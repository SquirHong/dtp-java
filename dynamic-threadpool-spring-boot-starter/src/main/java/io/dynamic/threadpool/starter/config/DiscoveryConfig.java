package io.dynamic.threadpool.starter.config;


import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.starter.core.DiscoveryClient;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;

import static io.dynamic.threadpool.common.constant.Constants.CLIENT_IDENTIFICATION_VALUE;
import static io.dynamic.threadpool.starter.toolkit.CloudCommonIdUtil.getDefaultInstanceId;
import static io.dynamic.threadpool.starter.toolkit.CloudCommonIdUtil.getIpApplicationName;


@AllArgsConstructor
public class DiscoveryConfig {

    private ConfigurableEnvironment environment;

    @Bean
    @SneakyThrows
    public InstanceInfo instanceConfig() {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setInstanceId(getDefaultInstanceId(environment))
                .setIpApplicationName(getIpApplicationName(environment))
                .setHostName(InetAddress.getLocalHost().getHostAddress())
                .setIdentify(CLIENT_IDENTIFICATION_VALUE)
                .setAppName(environment.getProperty("spring.application.name"))
                .setClientBasePath(environment.getProperty("server.servlet.context-path"));
        String callBackUrl = new StringBuilder()
                .append(instanceInfo.getHostName())
                .append(":")
                .append(environment.getProperty("server.port"))
                .append(instanceInfo.getClientBasePath())
                .toString();
        instanceInfo.setCallBackUrl(callBackUrl);

        return instanceInfo;
    }

    @Bean
    public DiscoveryClient discoveryClient(HttpAgent httpAgent, InstanceInfo instanceInfo) {
        return new DiscoveryClient(httpAgent, instanceInfo);
    }

}
