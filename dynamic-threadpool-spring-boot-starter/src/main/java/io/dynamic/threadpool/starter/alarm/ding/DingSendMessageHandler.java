package io.dynamic.threadpool.starter.alarm.ding;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.alarm.NotifyDTO;
import io.dynamic.threadpool.starter.alarm.NotifyPlatformEnum;
import io.dynamic.threadpool.starter.alarm.SendMessageHandler;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.dynamic.threadpool.starter.alarm.ding.DingAlarmConstants.*;

// TODO: 2024/10/16  当前页面配置的机器人hook发送消息不支持实现签名校验
@Slf4j
@AllArgsConstructor
@Component
public class DingSendMessageHandler implements SendMessageHandler {

    private String active;

    private InstanceInfo instanceInfo;

    private static final String secret = "SECb7b4f4e8e1b7a5b27b096e036973c2f648d4da94d85fea403bf3cc85b5c5f2be";

    private static final Mac MAC_INSTANCE;

    static {
        try {
            MAC_INSTANCE = Mac.getInstance("HmacSHA256");
            MAC_INSTANCE.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public String getType() {
        return NotifyPlatformEnum.DING.name();
    }

    @Override
    public void sendAlarmMessage(NotifyDTO notifyConfig, DynamicThreadPoolExecutor pool) {
        dingAlarmSendMessage(notifyConfig, pool);
    }

    @Override
    public void sendChangeMessage(NotifyDTO notifyConfig, PoolParameterInfo parameter) {
        dingChangeSendMessage(notifyConfig, parameter);
    }

    public void dingAlarmSendMessage(NotifyDTO notifyConfig, DynamicThreadPoolExecutor pool) {
        List<String> receives = StrUtil.split(notifyConfig.getReceives(), ',');
        String afterReceives = Joiner.on(", @").join(receives);

        BlockingQueue<Runnable> queue = pool.getQueue();
        String text = String.format(
                DING_ALARM_TXT,
                // 环境
                active.toUpperCase(),
                // 节点信息
                instanceInfo.getIpApplicationName(),
                // 报警类型
                notifyConfig.getTypeEnum(),
                // 线程池ID
                pool.getThreadPoolId(),
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
                // 告警手机号
                afterReceives,
                // 报警频率
                notifyConfig.getInterval(),
                // 当前时间
                DateUtil.now()

        );

        try {
            execute(notifyConfig, DING_ALARM_TITLE, text, receives);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dingChangeSendMessage(NotifyDTO notifyConfig, PoolParameterInfo parameter) {
        String threadPoolId = parameter.getTpId();
        DynamicThreadPoolWrapper poolWrap = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        if (poolWrap == null) {
            log.warn("Thread pool is empty when sending change notification, threadPoolId :: {}", threadPoolId);
            return;
        }
        List<String> receives = StrUtil.split(notifyConfig.getReceives(), ',');
        String afterReceives = Joiner.on(", @").join(receives);

        ThreadPoolExecutor customPool = poolWrap.getExecutor();

        long agoAliveTime = customPool.getKeepAliveTime(TimeUnit.SECONDS);
        long nowAliveTime = parameter.getKeepAliveTime();
        int agoCapacity = customPool.getQueue().size() + customPool.getQueue().remainingCapacity();
        String agoRejectedName = customPool.getRejectedExecutionHandler().getClass().getSimpleName();
        String nowRejectedName = RejectedTypeEnum.getRejectedNameByType(parameter.getRejectedType());

        String text = String.format(
                DING_NOTICE_TXT,
                // 环境
                active.toUpperCase(),
                // 线程池名称
                threadPoolId,
                // 节点信息
                instanceInfo.getIpApplicationName(),
                // 核心线程数
                customPool.getCorePoolSize() + (customPool.getCorePoolSize() == parameter.getCoreSize() ? "  未变化" :
                        "  ➲  " + parameter.getCoreSize()),
                // 最大线程数
                customPool.getMaximumPoolSize() + (customPool.getMaximumPoolSize() == parameter.getMaxSize() ? "  未变化" :
                        "  ➲  " + parameter.getMaxSize()),
                // 线程存活时间
                agoAliveTime + (agoAliveTime == nowAliveTime ? "  未变化" : "  ➲  " + nowAliveTime),
                // 阻塞队列
                // TODO: 2024/6/28 这里好像一直是ResizableCapacityLinkedBlockIngQueue.
                QueueTypeEnum.getBlockingQueueNameByType(parameter.getQueueType()),
                // 阻塞队列容量
                agoCapacity + (agoCapacity == parameter.getCapacity() ? "  未变化" : "  ➲  " + parameter.getCapacity()),
                // 拒绝策略
                agoRejectedName + (agoRejectedName.equals(nowRejectedName) ? "  未变化" : "  ➲  " + nowRejectedName),
                // 告警手机号
                afterReceives,
                // 当前时间
                DateUtil.now()
        );

        try {
            execute(notifyConfig, DING_NOTICE_TITLE, text, receives);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(NotifyDTO notifyConfig, String title, String text, List<String> mobiles) throws Exception {
        String url = DING_ROBOT_SERVER_URL;
        // 加签
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        byte[] signData = MAC_INSTANCE.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

        String serverUrl = url + notifyConfig.getSecretKey() + "&timestamp=" + timestamp + "&sign=" + sign;

        DingTalkClient dingTalkClient = new DefaultDingTalkClient(serverUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");

        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(mobiles);

        request.setAt(at);
        request.setMarkdown(markdown);

        try {
            dingTalkClient.execute(request);
        } catch (ApiException ex) {
            log.error("Ding failed to send message:{}", ex.getMessage());
        }
    }

}
