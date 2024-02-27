package io.dynamic.threadpool.config.service;

import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.notify.NotifyCenter;

/**
 * Config Change Publisher.
 */
public class ConfigChangePublisher {


    public static void notifyConfigChange(LocalDataChangeEvent event) {
        NotifyCenter.publishEvent(event);
    }
}
