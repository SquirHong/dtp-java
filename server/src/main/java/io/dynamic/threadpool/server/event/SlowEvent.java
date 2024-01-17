package io.dynamic.threadpool.server.event;

import io.dynamic.threadpool.server.event.Event;

/**
 * Slow Event.
 */
public abstract class SlowEvent extends Event {
    @Override
    public long sequence() {
        return 0;
    }
}
