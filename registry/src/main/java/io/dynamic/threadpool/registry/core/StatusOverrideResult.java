package io.dynamic.threadpool.registry.core;

import io.dynamic.threadpool.common.model.InstanceInfo;

public class StatusOverrideResult {

    public static StatusOverrideResult NO_MATCH = new StatusOverrideResult(false, null);

    public static StatusOverrideResult matchingStatus(InstanceInfo.InstanceStatus status) {
        return new StatusOverrideResult(true, status);
    }

    private final boolean matches;

    private final InstanceInfo.InstanceStatus status;

    private StatusOverrideResult(boolean matches, InstanceInfo.InstanceStatus status) {
        this.matches = matches;
        this.status = status;
    }

    public boolean matches() {
        return matches;
    }

    public InstanceInfo.InstanceStatus status() {
        return status;
    }

}
