package io.dynamic.threadpool.starter.monitor;


import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import io.dynamic.threadpool.common.model.PoolRunStateInfo;
import io.dynamic.threadpool.common.monitor.AbstractMessage;
import io.dynamic.threadpool.common.monitor.Message;
import io.dynamic.threadpool.common.monitor.MessageTypeEnum;
import io.dynamic.threadpool.common.monitor.RuntimeMessage;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.handle.AbstractThreadPoolRuntime;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadFactoryBuilder;
import io.dynamic.threadpool.starter.toolkit.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.dynamic.threadpool.starter.core.GlobalThreadPoolManage.getThreadPoolNum;
import static io.dynamic.threadpool.starter.toolkit.IdentifyUtil.getThreadPoolIdentify;


/**
 * 动态线程池采集上报事件执行器.
 * {@link BlockingQueue} 充当缓冲容器, 实现生产-消费模型.
 */
@Slf4j
@RequiredArgsConstructor
public class ReportingEventExecutor extends AbstractThreadPoolRuntime implements Runnable, Collect, CommandLineRunner {


    @Resource
    private BootstrapProperties properties;

    @Resource
    private HttpMvcSender httpMvcSender;

    /**
     * 数据采集的缓冲容器, 等待 ReportingEventExecutor 上报服务端
     */
    private final BlockingQueue<Message> messageCollectVessel = new ArrayBlockingQueue(4096);

    /**
     * 数据采集定时执行器, Spring 启动后延时一段时间进行采集动态线程池的运行数据
     */
    private final ScheduledThreadPoolExecutor collectVesselExecutor = new ScheduledThreadPoolExecutor(
            new Integer(1),
            ThreadFactoryBuilder.builder().daemon(true).prefix("scheduled-collect-vessel").build()
    );

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            try {
                Message message = messageCollectVessel.take();
                httpMvcSender.send(message);
            } catch (Throwable ex) {
                log.error("Consumption buffer container task failed. Number of buffer container tasks :: {}", messageCollectVessel.size(), ex);
            }
        }
    }

    @Override
    public void run(String... args) {
        // 延迟 10秒后每 5秒调用一次. scheduleWithFixedDelay 每次执行时间为上一次任务结束时, 向后推一个时间间隔
        collectVesselExecutor.scheduleWithFixedDelay(() -> runTimeGatherTask(), 10, 5, TimeUnit.SECONDS);
        ThreadUtil.newThread(this, "reporting-task", Boolean.TRUE).start();

        log.info("Dynamic thread pool :: [{}]. The dynamic thread pool starts data collection and reporting. ", getThreadPoolNum());
    }

    /**
     * 采集动态线程池数据, 并添加缓冲队列
     */
    private void runTimeGatherTask() {
        Message message = collectMessage();
        messageCollectVessel.offer(message);
    }

    @Override
    public Message collectMessage() {
        AbstractMessage messageResult = new RuntimeMessage();

        List<Message> runtimeMessages = Lists.newArrayList();
        List<String> listThreadPoolId = GlobalThreadPoolManage.listThreadPoolId();
        for (String each : listThreadPoolId) {

            PoolRunStateInfo poolRunState = getPoolRunState(each);
            RuntimeMessage runtimeMessage = BeanUtil.toBean(poolRunState, RuntimeMessage.class);
            runtimeMessage.setGroupKey(getThreadPoolIdentify(each, properties));
            runtimeMessages.add(runtimeMessage);
        }

        messageResult.setMessageType(MessageTypeEnum.RUNTIME);
        messageResult.setMessages(runtimeMessages);

        return messageResult;
    }

    @Override
    protected PoolRunStateInfo supplement(PoolRunStateInfo poolRunStateInfo) {
        return poolRunStateInfo;
    }

}
