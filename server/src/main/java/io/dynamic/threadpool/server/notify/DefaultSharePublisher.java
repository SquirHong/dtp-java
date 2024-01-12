package io.dynamic.threadpool.server.notify;

import cn.hutool.core.collection.ConcurrentHashSet;
import io.dynamic.threadpool.server.notify.listener.Subscriber;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Default Share Publisher.
 */
public class DefaultSharePublisher {

    private final Map<Class<? extends SlowEvent>, Set<Subscriber>> subMappings = new ConcurrentHashMap();

    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet();

    private final Lock lock = new ReentrantLock();

    /**
     * 添加订阅者
     * @param subscriber   订阅者
     * @param subscribeType 想订阅的事件
     */
    public void addSubscriber(Subscriber subscriber, Class<? extends Event> subscribeType) {
        Class<? extends SlowEvent> subSlowEventType = (Class<? extends SlowEvent>) subscribeType;
        subscribers.add(subscriber);

        lock.lock();
        try {
            Set<Subscriber> sets = subMappings.get(subSlowEventType);
            if (sets == null) {
                Set<Subscriber> newSet = new ConcurrentHashSet();
                newSet.add(subscriber);
                subMappings.put(subSlowEventType, newSet);
                return;
            }
            sets.add(subscriber);
        } finally {
            lock.unlock();
        }
    }
}
