package io.dynamic.threadpool.config.event;

/**
 * Local Data Change Event.
 */
public class LocalDataChangeEvent extends Event {

    public final String groupKey;

    public LocalDataChangeEvent(String groupKey) {
        this.groupKey = groupKey;
    }
}
