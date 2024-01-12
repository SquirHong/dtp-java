package io.dynamic.threadpool.server.notify;

import cn.hutool.core.collection.ConcurrentHashSet;
import io.dynamic.threadpool.server.notify.listener.Subscriber;

/**
 * 默认事件发布器
 */
public class DefaultPublisher extends Thread implements EventPublisher {

    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet();

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}
