package io.dynamic.threadpool.starter.config;

import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.starter.alarm.AlarmControlHandler;
import io.dynamic.threadpool.starter.alarm.BaseSendMessageService;
import io.dynamic.threadpool.starter.alarm.SendMessageHandler;
import io.dynamic.threadpool.starter.alarm.SendMessageService;
import io.dynamic.threadpool.starter.alarm.ding.DingSendMessageHandler;
import io.dynamic.threadpool.starter.alarm.lark.LarkSendMessageHandler;
import io.dynamic.threadpool.starter.remote.HttpAgent;
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

    public static final String SEND_MESSAGE_BEAN_NAME = "dtpSendMessageService";

    @DependsOn("dtpApplicationContextHolder")
    @Bean(SEND_MESSAGE_BEAN_NAME)
    public SendMessageService dtpSendMessageService(HttpAgent httpAgent, AlarmControlHandler alarmControlHandler) {
        return new BaseSendMessageService(httpAgent, properties, alarmControlHandler);
    }

    // 注入报警控制处理器
    @Bean
    public AlarmControlHandler alarmControlHandler() {
        return new AlarmControlHandler();
    }

    // 注入钉钉消息通知处理器
    @Bean
    public SendMessageHandler dingSendMessageHandler() {
        String active = environment.getProperty("spring.profiles.active", Strings.EMPTY);
        return new DingSendMessageHandler(active, instanceInfo);
    }

    // 注入lark消息通知处理器
    @Bean
    public SendMessageHandler larkSendMessageHandler() {
        String active = environment.getProperty("spring.profiles.active", Strings.EMPTY);
        return new LarkSendMessageHandler(active, instanceInfo);
    }

}
