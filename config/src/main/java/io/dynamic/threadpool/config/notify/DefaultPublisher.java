package io.dynamic.threadpool.config.notify;

import cn.hutool.core.collection.ConcurrentHashSet;
import io.dynamic.threadpool.config.event.Event;
import io.dynamic.threadpool.config.notify.listener.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 默认事件发布器
 */
@Slf4j
public class DefaultPublisher extends Thread implements EventPublisher {

    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet();

    private BlockingQueue<Event> queue;

    private volatile boolean initialized = false;

    private volatile boolean shutdown = false;

    private int queueMaxSize = -1;

    protected volatile Long lastEventSequence = -1L;

    private static final AtomicReferenceFieldUpdater<DefaultPublisher, Long> UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPublisher.class, Long.class, "lastEventSequence");

    @Override
    public void init(Class<? extends Event> type, int bufferSize) {
        setDaemon(true);
        setName("dynamic.thread-pool.publisher-" + type.getName());
        this.queueMaxSize = bufferSize;
        this.queue = new ArrayBlockingQueue(queueMaxSize);
        start();
    }

    @Override
    public synchronized void start() {

        super.start();
        if (queueMaxSize == -1) {
            queueMaxSize = NotifyCenter.ringBufferSize;
        }
        initialized = true;

    }

    @Override
    public void run() {
        openEventHandler();
    }

    private void openEventHandler() {
        try {
            int waitTimes = 60;
            for (; ; ) {
                if (shutdown || hasSubscriber() || waitTimes <= 0) {
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                waitTimes--;
            }

            for (; ; ) {
                if (shutdown) {
                    break;
                }
                Event event = queue.take();
                receiveEvent(event);
                UPDATER.compareAndSet(this, lastEventSequence, Math.max(lastEventSequence, event.sequence()));
            }
        } catch (Throwable ex) {
            log.error("Event listener exception : {}", ex);
        }
    }

    @Override
    public boolean publish(Event event) {
        boolean success = this.queue.offer(event);
        if (!success) {
            log.warn("Unable to plug in due to interruption, event : {}", event);
            receiveEvent(event);
            return true;
        }
        return true;
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {
        final Runnable job = () -> subscriber.onEvent(event);

        Executor executor = subscriber.executor();

        if (executor != null) {
            executor.execute(job);
        } else {
            // 没有处理器，直接 run
            try {
                job.run();
            } catch (Throwable e) {
                log.error("Event callback exception : {}", e);
            }
        }
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    private boolean hasSubscriber() {
        return !CollectionUtils.isEmpty(subscribers);
    }

    void receiveEvent(Event event) {

        for (Subscriber subscriber : subscribers) {
            notifySubscriber(subscriber, event);
        }
    }


}
