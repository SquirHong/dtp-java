package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.starter.alarm.BaseSendMessageService;
import io.dynamic.threadpool.starter.alarm.DingSendMessageHandler;
import io.dynamic.threadpool.starter.alarm.SendMessageHandler;
import io.dynamic.threadpool.starter.alarm.SendMessageService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;

@AllArgsConstructor
public class MessageAlarmConfig {

    private final BootstrapProperties properties;

    private final InstanceInfo instanceInfo;

    private ConfigurableEnvironment environment;

    @Bean
    @DependsOn("applicationContextHolder")
    public SendMessageService sendMessageService() {
        return new BaseSendMessageService(properties.getNotifys());
    }

    // 注入钉钉消息通知处理器
    @Bean
    public SendMessageHandler dingSendMessageHandler() {
        String active = environment.getProperty("spring.profiles.active", Strings.EMPTY);
        return new DingSendMessageHandler(active, instanceInfo);
    }

}
