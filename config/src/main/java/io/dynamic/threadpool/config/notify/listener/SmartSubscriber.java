package io.dynamic.threadpool.config.notify.listener;

import io.dynamic.threadpool.config.event.Event;

import java.util.List;

/**
 * Subscribers to multiple events can be listened to.
 */
public abstract class SmartSubscriber extends Subscriber {

    public abstract List<Class<? extends Event>> subscribeTypes();
}
