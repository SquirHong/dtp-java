package io.dynamic.threadpool.server.notify;

import io.dynamic.threadpool.server.notify.listener.Subscriber;

/**
 * Event publisher.
 */
public interface EventPublisher {

    void addSubscriber(Subscriber subscriber);

}
