package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.starter.alarm.*;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Optional;

@AllArgsConstructor
public class MessageAlarmConfig {

    private final BootstrapProperties properties;

    private final InstanceInfo instanceInfo;

    private ConfigurableEnvironment environment;

    public static final String SEND_MESSAGE_BEAN_NAME = "sendMessageService";

    @DependsOn("applicationContextHolder")
    @Bean(SEND_MESSAGE_BEAN_NAME)
    public SendMessageService sendMessageService(HttpAgent httpAgent) {
        return new BaseSendMessageService(httpAgent, properties);
    }

    // 注入报警控制处理器
    @Bean
    public AlarmControlHandler alarmControlHandler() {
        Long alarmInterval = properties.getAlarmInterval();
        return new AlarmControlHandler(alarmInterval);
    }

    // 注入钉钉消息通知处理器
    @Bean
    public SendMessageHandler dingSendMessageHandler() {
        String active = environment.getProperty("spring.profiles.active", Strings.EMPTY);
        return new DingSendMessageHandler(active, instanceInfo);
    }

}
