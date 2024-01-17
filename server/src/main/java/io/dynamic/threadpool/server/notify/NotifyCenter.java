package io.dynamic.threadpool.server.notify;

import io.dynamic.threadpool.server.event.Event;
import io.dynamic.threadpool.server.event.SlowEvent;
import io.dynamic.threadpool.server.notify.listener.SmartSubscriber;
import io.dynamic.threadpool.server.notify.listener.Subscriber;
import io.dynamic.threadpool.server.toolkit.ClassUtil;
import io.dynamic.threadpool.server.toolkit.MapUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * 统一事件通知中心
 */
@Slf4j
public class NotifyCenter {

    private static final NotifyCenter INSTANCE = new NotifyCenter();

    public static int ringBufferSize = 16384;

    public static int shareBufferSize = 1024;

    private DefaultSharePublisher sharePublisher;

    private static Class<? extends EventPublisher> clazz = null;

    private static EventPublisher eventPublisher = new DefaultPublisher();

    private static BiFunction<Class<? extends Event>, Integer, EventPublisher> publisherFactory = null;

    private final Map<String, EventPublisher> publisherMap = new ConcurrentHashMap();

    static {
        publisherFactory = (cls, buffer) -> {
            try {
                EventPublisher publisher = eventPublisher;
                publisher.init(cls, buffer);
                return publisher;
            } catch (Throwable ex) {
                log.error("Service class newInstance has error : {}", ex);
                throw new RuntimeException(ex);
            }
        };

        INSTANCE.sharePublisher = new DefaultSharePublisher();
        INSTANCE.sharePublisher.init(SlowEvent.class, shareBufferSize);
    }

    /**
     * 将订阅者注册到事件发布者中
     */
    public static void registerSubscriber(final Subscriber consumer) {
        if (consumer instanceof SmartSubscriber) {
            for (Class<? extends Event> subscribeType : ((SmartSubscriber) consumer).subscribeTypes()) {
                if (ClassUtil.isAssignableFrom(SlowEvent.class, subscribeType)) {
                    INSTANCE.sharePublisher.addSubscriber(consumer, subscribeType);
                } else {
                    addSubscriber(consumer, subscribeType);
                }
            }
            return;
        }

        final Class<? extends Event> subscribeType = consumer.subscribeType();
        if (ClassUtil.isAssignableFrom(SlowEvent.class, subscribeType)) {
            INSTANCE.sharePublisher.addSubscriber(consumer, subscribeType);
            return;
        }

        addSubscriber(consumer, subscribeType);
    }

    /**
     * 将订阅者注册到事件发布者中
     */
    private static void addSubscriber(final Subscriber consumer, Class<? extends Event> subscribeType) {

        final String topic = ClassUtil.getCanonicalName(subscribeType);
        synchronized (NotifyCenter.class) {
            MapUtil.computeIfAbsent(INSTANCE.publisherMap, topic, publisherFactory, subscribeType, ringBufferSize);
        }
        EventPublisher publisher = INSTANCE.publisherMap.get(topic);
        publisher.addSubscriber(consumer);
    }

    public static boolean publishEvent(final Event event) {
        try {
            return publishEvent(event.getClass(), event);
        } catch (Throwable ex) {
            log.error("There was an exception to the message publishing : {}", ex);
            return false;
        }
    }

    private static boolean publishEvent(final Class<? extends Event> eventType, final Event event) {
        if (ClassUtil.isAssignableFrom(SlowEvent.class, eventType)) {
            return INSTANCE.sharePublisher.publish(event);
        }

        final String topic = ClassUtil.getCanonicalName(eventType);

        EventPublisher publisher = INSTANCE.publisherMap.get(topic);
        if (publisher != null) {
            return publisher.publish(event);
        }
        log.warn("There are no [{}] publishers for this event, please register", topic);
        return false;
    }


    public static EventPublisher registerToPublisher(final Class<? extends Event> eventType, final int queueMaxSize) {
        if (ClassUtil.isAssignableFrom(SlowEvent.class, eventType)) {
            return INSTANCE.sharePublisher;
        }

        final String topic = ClassUtil.getCanonicalName(eventType);
        synchronized (NotifyCenter.class) {
            MapUtil.computeIfAbsent(INSTANCE.publisherMap, topic, publisherFactory, eventType, queueMaxSize);
        }
        return INSTANCE.publisherMap.get(topic);
    }

}
