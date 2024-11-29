package io.dynamic.threadpool.starter.monitor;


import io.dynamic.threadpool.common.monitor.Message;
import io.dynamic.threadpool.common.monitor.MessageWrapper;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import static io.dynamic.threadpool.common.constant.Constants.MONITOR_PATH;


@Slf4j
public class HttpMvcSender implements MessageSender {

    @Resource
    private HttpAgent httpAgent;

    @Override
    public void send(Message message) {
        try {
            MessageWrapper messageWrapper = new MessageWrapper(message);
            messageWrapper.setMessageType(message.getMessageType());
            httpAgent.httpPost(MONITOR_PATH, messageWrapper);
        } catch (Throwable ex) {
            log.error("Failed to push dynamic thread pool runtime data.", ex);
        }
    }

}
