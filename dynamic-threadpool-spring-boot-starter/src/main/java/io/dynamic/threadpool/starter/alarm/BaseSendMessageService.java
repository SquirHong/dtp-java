package io.dynamic.threadpool.starter.alarm;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.common.toolkit.GroupKey;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseSendMessageService implements InitializingBean, SendMessageService {

    @NonNull
    private final HttpAgent httpAgent;

    @NonNull
    private final BootstrapProperties properties;

    private static final Map<String, List<AlarmNotifyDTO>> ALARM_NOTIFY_CONFIG = Maps.newHashMap();

    // 平台：钉钉、飞书、OA办公等
    private final Map<String, SendMessageHandler> sendMessageHandlers = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() {
        Map<String, SendMessageHandler> sendMessageHandlerMap =
                ApplicationContextHolder.getBeansOfType(SendMessageHandler.class);
        sendMessageHandlerMap.values().forEach(each -> sendMessageHandlers.put(each.getType(), each));
        List<String> threadPoolIds = GlobalThreadPoolManage.listThreadPoolId();
        if (CollUtil.isEmpty(threadPoolIds)) {
            log.warn("The client does not have a dynamic thread pool instance configured.");
            return;
        }
        List<String> groupKeys = Lists.newArrayList();
        threadPoolIds.forEach(each -> {
            String groupKey = GroupKey.getKeyTenant(each, properties.getItemId(), properties.getTenantId());
            groupKeys.add(groupKey);
        });
        Result result = null;
        result = httpAgent.httpPostByDiscovery("/v1/cs/alarm/list/config", new ThreadPoolAlarmReqDTO(groupKeys));

        if (result.isSuccess() || result.getData() != null) {
            List<ThreadPoolAlarmNotify> resultData = JSON.parseArray(JSON.toJSONString(result.getData()), ThreadPoolAlarmNotify.class);
            resultData.forEach(each -> ALARM_NOTIFY_CONFIG.put(each.getThreadPoolId(), each.getAlarmNotifyList()));
        }
    }

    @Override
    public void sendAlarmMessage(DynamicThreadPoolExecutor threadPoolExecutor) {
        List<AlarmNotifyDTO> notifyList = ALARM_NOTIFY_CONFIG.get(threadPoolExecutor.getThreadPoolId());
        if (CollUtil.isEmpty(notifyList)) {
            log.warn("Please configure alarm notification on the server.");
            return;
        }

        notifyList.forEach(each -> {
            SendMessageHandler messageHandler = sendMessageHandlers.get(each.getPlatform());
            if (messageHandler == null) {
                log.warn("Please configure alarm notification for platform: " + each.getPlatform() + " on the server.");
                return;
            }
            messageHandler.sendAlarmMessage(each, threadPoolExecutor);
        });
    }

    @Override
    public void sendChangeMessage(PoolParameterInfo parameter) {
        List<AlarmNotifyDTO> notifyList = ALARM_NOTIFY_CONFIG.get(parameter.getTpId());
        if (CollUtil.isEmpty(notifyList)) {
            log.warn("Please configure alarm notification on the server.");
            return;
        }

        notifyList.forEach(each -> {
            SendMessageHandler messageHandler = sendMessageHandlers.get(each.getPlatform());
            if (messageHandler == null) {
                log.warn("Please configure alarm notification for platform: " + each.getPlatform() + " on the server.");
                return;
            }
            messageHandler.sendChangeMessage(each, parameter);
        });
    }

    @Data
    @AllArgsConstructor
    static class ThreadPoolAlarmReqDTO {

        /**
         * groupKeys
         */
        private List<String> groupKeys;

    }

    @Data
    static class ThreadPoolAlarmNotify {

        /**
         * 线程池ID
         */
        private String threadPoolId;

        /**
         * 报警配置
         */
        private List<AlarmNotifyDTO> alarmNotifyList;

    }

}
