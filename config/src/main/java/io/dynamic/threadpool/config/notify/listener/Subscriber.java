package io.dynamic.threadpool.config.notify.listener;

import io.dynamic.threadpool.config.event.Event;

import java.util.concurrent.Executor;

/**
 * An abstract subscriber class for subscriber interface.
 */
public abstract class Subscriber<T extends Event> {

    /**
     * Event callback.
     */
    public abstract void onEvent(T event);

    /**
     * Type of this subscriber's subscription.
     */
    public abstract Class<? extends Event> subscribeType();

    public Executor executor() {
        return null;
    }

}
