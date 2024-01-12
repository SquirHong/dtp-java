package io.dynamic.threadpool.server.notify;

/**
 * Slow Event.
 */
public abstract class SlowEvent extends Event {
    @Override
    public long sequence() {
        return 0;
    }
}
