package io.dynamic.threadpool.server.service;

import io.dynamic.threadpool.server.event.ConfigDataChangeEvent;
import io.dynamic.threadpool.server.event.LocalDataChangeEvent;
import io.dynamic.threadpool.server.notify.NotifyCenter;

/**
 * Config Change Publisher.
 */
public class ConfigChangePublisher {


    public static void notifyConfigChange(LocalDataChangeEvent event) {
        NotifyCenter.publishEvent(event);
    }
}
