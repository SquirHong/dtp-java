package io.dynamic.threadpool.starter.alarm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseSendMessageService implements InitializingBean, SendMessageService {

    @NonNull
    private final HttpAgent httpAgent;

    @NonNull
    private final BootstrapProperties properties;

    @NonNull
    private final AlarmControlHandler alarmControlHandler;

    private static final Map<String, List<NotifyDTO>> ALARM_NOTIFY_CONFIG = Maps.newHashMap();

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
        try {
            result = httpAgent.httpPostByDiscovery("/v1/cs/notify/list/config", new ThreadPoolNotifyReqDTO(groupKeys));
        } catch (Exception e) {
            log.error("Failed to get alarm notification configuration.", e);
        }

        if (result != null && result.isSuccess() && result.getData() != null) {
            List<ThreadPoolNotify> resultData = JSON.parseArray(JSON.toJSONString(result.getData()), ThreadPoolNotify.class);
            resultData.forEach(each -> ALARM_NOTIFY_CONFIG.put(each.getNotifyKey(), each.getNotifyList()));

            ALARM_NOTIFY_CONFIG.forEach((key, value) -> {
                value.stream().filter(each -> "ALARM".equals(each.getType())).forEach(each -> {
                    Cache<String, String> cache = CacheBuilder.newBuilder()
                            .expireAfterWrite(each.getInterval(), TimeUnit.MINUTES)
                            .build();
                    AlarmControlHandler.THREAD_POOL_ALARM_CACHE.put(StrUtil.builder(each.getTpId(), "+", each.getPlatform()).toString(), cache);
                });
            });

        }
    }

    @Override
    public void sendAlarmMessage(MessageTypeEnum typeEnum, DynamicThreadPoolExecutor threadPoolExecutor) {
        String buildKey = threadPoolExecutor.getThreadPoolId() + "+ALARM";
        List<NotifyDTO> notifyList = ALARM_NOTIFY_CONFIG.get(buildKey);
        if (CollUtil.isEmpty(notifyList)) {
            log.warn("Please configure alarm notification on the server.");
            return;
        }

        notifyList.forEach(each -> {
            SendMessageHandler messageHandler = sendMessageHandlers.get(each.getPlatform());
            if (messageHandler == null) {
                log.warn("服务端没有实现: " + each.getPlatform() + " 平台.");
                return;
            }
            if (isSendAlarm(each.getTpId(), each.setTypeEnum(typeEnum))) {
                messageHandler.sendAlarmMessage(each, threadPoolExecutor);
            }
        });
    }

    @Override
    public void sendChangeMessage(PoolParameterInfo parameter) {
        String buildKey = parameter.getTpId() + "+ALARM";
        List<NotifyDTO> notifyList = ALARM_NOTIFY_CONFIG.get(buildKey);
        if (CollUtil.isEmpty(notifyList)) {
            log.warn("Please configure alarm notification on the server.");
            return;
        }

        notifyList.forEach(each -> {
            SendMessageHandler messageHandler = sendMessageHandlers.get(each.getPlatform());
            if (messageHandler == null) {
                log.warn("服务端没有实现: " + each.getPlatform() + " 平台.");
                return;
            }
            messageHandler.sendChangeMessage(each, parameter);
        });
    }

    private boolean isSendAlarm(String threadPoolId, NotifyDTO notifyInfo) {
        AlarmControlDTO alarmControl = AlarmControlDTO.builder()
                .threadPool(threadPoolId)
                .platform(notifyInfo.getPlatform())
                .typeEnum(notifyInfo.getTypeEnum())
                .build();

        return alarmControlHandler.isSendAlarm(alarmControl);
    }

    @Data
    @AllArgsConstructor
    static class ThreadPoolNotifyReqDTO {

        /**
         * groupKeys
         */
        private List<String> groupKeys;

    }

    @Data
    static class ThreadPoolNotify {

        /**
         * 通知 Key tpid+notifyType
         */
        private String notifyKey;

        /**
         * 报警配置
         */
        private List<NotifyDTO> notifyList;

    }

}
