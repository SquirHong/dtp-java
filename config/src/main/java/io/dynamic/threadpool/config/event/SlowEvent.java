package io.dynamic.threadpool.config.event;

/**
 * Slow Event.
 */
public abstract class SlowEvent extends Event {
    @Override
    public long sequence() {
        return 0;
    }
}
