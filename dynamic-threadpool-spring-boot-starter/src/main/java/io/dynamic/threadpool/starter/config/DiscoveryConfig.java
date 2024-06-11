package io.dynamic.threadpool.starter.config;


import io.dynamic.threadpool.starter.core.DiscoveryClient;
import io.dynamic.threadpool.starter.core.InstanceConfig;
import io.dynamic.threadpool.starter.core.InstanceInfo;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import io.dynamic.threadpool.starter.toolkit.CloudCommonIdUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;



@AllArgsConstructor
public class DiscoveryConfig {

    private ConfigurableEnvironment environment;

    @Bean
    public InstanceConfig instanceConfig() {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setInstanceId(CloudCommonIdUtil.getDefaultInstanceId(environment));

        String hostNameKey = "eureka.instance.hostname";
        String hostNameVal = environment.containsProperty(hostNameKey) ? environment.getProperty(hostNameKey) : "";
        instanceInfo.setHostName(hostNameVal);

        return instanceInfo;
    }

    @Bean
    public DiscoveryClient discoveryClient(HttpAgent httpAgent, InstanceConfig instanceConfig) {
        return new DiscoveryClient(httpAgent, instanceConfig);
    }

}
