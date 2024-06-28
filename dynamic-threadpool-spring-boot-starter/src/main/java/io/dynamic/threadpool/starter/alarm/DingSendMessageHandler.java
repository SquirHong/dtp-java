package io.dynamic.threadpool.starter.alarm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.thread.CustomThreadPoolExecutor;
import io.dynamic.threadpool.starter.toolkit.thread.QueueTypeEnum;
import io.dynamic.threadpool.starter.toolkit.thread.RejectedTypeEnum;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class DingSendMessageHandler implements SendMessageHandler {

    @NonNull
    private String active;

    @NonNull
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
        return SendMessageEnum.DING.name();
    }

    @Override
    public void sendAlarmMessage(List<AlarmConfig> alarmConfigs, CustomThreadPoolExecutor pool) {
        Optional<AlarmConfig> alarmConfigOptional = alarmConfigs.stream()
                .filter(each -> Objects.equals(each.getType(), getType()))
                .findFirst();
        alarmConfigOptional.ifPresent(each -> {
            try {
                dingAlarmSendMessage(each, pool);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void sendChangeMessage(List<AlarmConfig> alarmConfigs, PoolParameterInfo parameter) {
        Optional<AlarmConfig> changeConfigOptional = alarmConfigs.stream()
                .filter(each -> Objects.equals(each.getType(), getType()))
                .findFirst();
        changeConfigOptional.ifPresent(each -> dingChangeSendMessage(each, parameter));
    }

    public void dingAlarmSendMessage(AlarmConfig alarmConfig, CustomThreadPoolExecutor pool) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        BlockingQueue<Runnable> queue = pool.getQueue();
        String text = String.format(
                "<font color='#FF0000'>[警报] </font>%s - 动态线程池运行告警 \n\n" +
                        " --- \n\n " +
                        "<font color='#778899' size=2>应用实例：%s</font> \n\n " +
                        "<font color='#708090' size=2>线程池名称：%s</font> \n\n " +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>核心线程数：%d</font> \n\n " +
                        "<font color='#708090' size=2>最大线程数：%d</font> \n\n " +
                        "<font color='#708090' size=2>当前线程数：%d</font> \n\n " +
                        "<font color='#708090' size=2>活跃线程数：%d</font> \n\n " +
                        "<font color='#708090' size=2>最大线程数：%d</font> \n\n " +
                        "<font color='#708090' size=2>线程池任务总量：%d</font> \n\n " +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>队列类型：%s</font> \n\n " +
                        "<font color='#708090' size=2>队列容量：%d</font> \n\n " +
                        "<font color='#708090' size=2>队列元素个数：%d</font> \n\n " +
                        "<font color='#708090' size=2>队列剩余个数：%d</font> \n\n " +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>拒绝策略：%s</font> \n\n" +
                        "<font color='#708090' size=2>拒绝策略执行次数：</font><font color='#FF0000' size=2>%d</font> \n\n " +
                        "<font color='#708090' size=2>OWNER：@%s</font> \n\n" +
                        "<font color='#708090' size=2>提示：5 分钟内此线程池不会重复告警（可配置）</font> \n\n" +
                        " --- \n\n  " +
                        "**播报时间：%s**",

                // 环境
                active.toUpperCase(),
                // 节点信息
                instanceInfo.getIpApplicationName(),
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
                "15926772290",
                // 当前时间
                DateUtil.now()

        );

        try {
            execute(alarmConfig, "动态线程池告警", text, CollUtil.newArrayList("15926772290"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dingChangeSendMessage(AlarmConfig alarmConfig, PoolParameterInfo parameter) {
        String threadPoolId = parameter.getTpId();
        DynamicThreadPoolWrap poolWrap = GlobalThreadPoolManage.getExecutorService(threadPoolId);
        if (poolWrap == null) {
            log.warn("Thread pool is empty when sending change notification, threadPoolId :: {}", threadPoolId);
            return;
        }

        CustomThreadPoolExecutor customPool = poolWrap.getPool();

        long agoAliveTime = customPool.getKeepAliveTime(TimeUnit.SECONDS);
        long nowAliveTime = parameter.getKeepAliveTime();
        int agoCapacity = customPool.getQueue().size() + customPool.getQueue().remainingCapacity();
        String agoRejectedName = customPool.getRejectedExecutionHandler().getClass().getSimpleName();
        String nowRejectedName = RejectedTypeEnum.getRejectedNameByType(parameter.getRejectedType());

        String text = String.format(
                "<font color='#2a9d8f'>[通知] </font>%s - 动态线程池参数变更 \n\n" +
                        " --- \n\n " +
                        "<font color='#708090' size=2>线程池ID：%s</font> \n\n " +
                        "<font color='#778899' size=2>应用实例：%s</font> \n\n " +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>核心线程数：%s</font> \n\n " +
                        "<font color='#708090' size=2>最大线程数：%s</font> \n\n " +
                        "<font color='#708090' size=2>线程存活时间(SECONDS)：%s </font> \n\n" +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>队列类型：%s</font> \n\n " +
                        "<font color='#708090' size=2>队列容量：%s</font> \n\n " +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>拒绝策略：%s</font> \n\n" +
                        " --- \n\n  " +
                        "<font color='#708090' size=2>提示：动态线程池配置变更实时通知（无限制）</font> \n\n" +
                        "<font color='#708090' size=2>OWNER：@%s</font> \n\n" +
                        " --- \n\n  " +
                        "**播报时间：%s**",
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
                "15926772290",
                // 当前时间
                DateUtil.now()
        );

        try {
            execute(alarmConfig, "动态线程池通知", text, CollUtil.newArrayList("15926772290"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(AlarmConfig alarmConfig, String title, String text, List<String> mobiles) throws Exception {

        // 加签
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        byte[] signData = MAC_INSTANCE.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

        String serverUrl = alarmConfig.getUrl() + alarmConfig.getToken() + "&timestamp=" + timestamp + "&sign=" + sign;

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
            log.error("Ding failed to send message", ex.getMessage());
        }
    }

}
