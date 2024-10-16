package io.dynamic.threadpool.starter.alarm.lark;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.alarm.NotifyDTO;
import io.dynamic.threadpool.starter.alarm.NotifyPlatformEnum;
import io.dynamic.threadpool.starter.alarm.SendMessageHandler;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.HttpClientUtil;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.dynamic.threadpool.starter.alarm.lark.LarkAlarmConstants.*;


/**
 * Send lark notification message.
 */
@Slf4j
@AllArgsConstructor
@Component
public class LarkSendMessageHandler implements SendMessageHandler {

    private String active;

    private InstanceInfo instanceInfo;

    private HttpClientUtil httpClientUtil;

    @Override
    public String getType() {
        return NotifyPlatformEnum.LARK.name();
    }

    @Override
    public void sendAlarmMessage(NotifyDTO notifyConfig, DynamicThreadPoolExecutor pool) {
        larkAlarmSendMessage(notifyConfig, pool);
    }

    @Override
    public void sendChangeMessage(NotifyDTO notifyConfig, PoolParameterInfo parameter) {
        larkChangeSendMessage(notifyConfig, parameter);
    }

    @SneakyThrows
    private void larkAlarmSendMessage(NotifyDTO notifyConfig, DynamicThreadPoolExecutor pool) {
        String afterReceives = getReceives(notifyConfig);

        BlockingQueue<Runnable> queue = pool.getQueue();

        String larkAlarmJson = getLarkJson(ALARM_JSON_PATH);

        String text = String.format(larkAlarmJson,
                // 环境
                active.toUpperCase(),
                // 线程池ID
                pool.getThreadPoolId(),
                // 应用名称
                instanceInfo.getAppName(),
                // 实例信息
                instanceInfo.getIdentify(),
                // 报警类型
                notifyConfig.getTypeEnum(),
                // 核心线程数
                pool.getCorePoolSize(),
                // 最大线程数
                pool.getMaximumPoolSize(),
                // 当前线程数
                pool.getPoolSize(),
                // 活跃线程数
                pool.getActiveCount(),
                // 最大任务数
                pool.getLargestPoolSize(),
                // 线程池任务总量
                pool.getCompletedTaskCount(),
                // 队列类型名称
                queue.getClass().getSimpleName(),
                // 队列容量
                queue.size() + queue.remainingCapacity(),
                // 队列元素个数
                queue.size(),
                // 队列剩余个数
                queue.remainingCapacity(),
                // 拒绝策略名称
                pool.getRejectedExecutionHandler().getClass().getSimpleName(),
                // 拒绝策略次数
                pool.getRejectCount(),
                // 告警姓名
                afterReceives,
                // 当前时间
                DateUtil.now(),
                // 报警频率
                notifyConfig.getInterval()
        );

        execute(notifyConfig, text);
    }

    @SneakyThrows
    private void larkChangeSendMessage(NotifyDTO notifyConfig, PoolParameterInfo parameter) {
        String threadPoolId = parameter.getTpId();
        DynamicThreadPoolWrapper poolWrap = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        if (poolWrap == null) {
            log.error("Thread pool is empty when sending change notification, threadPoolId :: {}", threadPoolId);
            return;
        }

        String afterReceives = getReceives(notifyConfig);

        ThreadPoolExecutor customPool = poolWrap.getExecutor();

        String larkNoticeJson = getLarkJson(NOTICE_JSON_PATH);

        /**
         * hesitant e.g. ➲  ➜  ⇨  ➪
         */
        String text = String.format(larkNoticeJson,
                // 环境
                active.toUpperCase(),
                // 线程池名称
                threadPoolId,
                // 应用名称
                instanceInfo.getAppName(),
                // 实例信息
                instanceInfo.getIdentify(),
                // 核心线程数
                customPool.getCorePoolSize() + "  ➲  " + parameter.getCoreSize(),
                // 最大线程数
                customPool.getMaximumPoolSize() + "  ➲  " + parameter.getMaxSize(),
                // 线程存活时间
                customPool.getKeepAliveTime(TimeUnit.SECONDS) + "  ➲  " + parameter.getKeepAliveTime(),
                // 阻塞队列
                QueueTypeEnum.getBlockingQueueNameByType(parameter.getQueueType()),
                // 阻塞队列容量
                (customPool.getQueue().size() + customPool.getQueue().remainingCapacity()) + "  ➲  " + parameter.getCapacity(),
                // 拒绝策略
                customPool.getRejectedExecutionHandler().getClass().getSimpleName(),
                RejectedTypeEnum.getRejectedNameByType(parameter.getRejectedType()),
                // 告警姓名
                afterReceives,
                // 当前时间
                DateUtil.now()
        );

        execute(notifyConfig, text);
    }

    private String getLarkJson(String filePath) throws FileNotFoundException {
        return FileUtil.readString(ResourceUtils.getFile(filePath), StandardCharsets.UTF_8);
    }

    private String getReceives(NotifyDTO notifyConfig) {
        if (StrUtil.isBlank(notifyConfig.getReceives())) {
            return "";
        }
        return Arrays.stream(notifyConfig.getReceives().split(","))
                .map(receive -> String.format(LARK_AT_FORMAT, receive))
                .collect(Collectors.joining(" "));
    }

    private void execute(NotifyDTO notifyConfig, String text) {
        String serverUrl = LARK_BOT_URL + notifyConfig.getSecretKey();

        try {
            httpClientUtil.restApiPost(serverUrl, text);
        } catch (Exception ex) {
            log.error("Lark failed to send message", ex);
        }
    }

}
