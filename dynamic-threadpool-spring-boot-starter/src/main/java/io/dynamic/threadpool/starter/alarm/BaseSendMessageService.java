package io.dynamic.threadpool.starter.alarm;

import cn.hutool.log.Log;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseSendMessageService implements InitializingBean, SendMessageService {

    @NonNull
    private final List<NotifyConfig> notifyConfigs;

    private final List<SendMessageHandler> sendMessageHandlers = new ArrayList(4);

    @Override
    public void afterPropertiesSet() {
        log.info("BaseSendMessageService init before");
        Map<String, SendMessageHandler> sendMessageHandlerMap =
                ApplicationContextHolder.getBeansOfType(SendMessageHandler.class);
        sendMessageHandlerMap.values().forEach(each -> sendMessageHandlers.add(each));
        log.info("BaseSendMessageService init after");
    }

    @Override
    public void sendAlarmMessage(CustomThreadPoolExecutor threadPoolExecutor) {
        for (SendMessageHandler messageHandler : sendMessageHandlers) {
            try {
                messageHandler.sendAlarmMessage(notifyConfigs, threadPoolExecutor);
            } catch (Exception ex) {
                log.warn("Failed to send thread pool alarm notification.", ex);
            }
        }
    }

    @Override
    public void sendChangeMessage(PoolParameterInfo parameter) {
        for (SendMessageHandler messageHandler : sendMessageHandlers) {
            try {
                messageHandler.sendChangeMessage(notifyConfigs, parameter);
            } catch (Exception ex) {
                log.warn("Failed to send thread pool change notification.", ex);
            }
        }
    }

}
