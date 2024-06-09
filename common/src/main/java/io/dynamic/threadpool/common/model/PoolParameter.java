package io.dynamic.threadpool.common.model;

/**
 * Pool Parameter.
 */
public interface PoolParameter {

    String getTenantId();

    String getItemId();

    String getTpId();

    Integer getCoreSize();

    Integer getMaxSize();

    Integer getQueueType();

    Integer getCapacity();

    Integer getKeepAliveTime();

    Integer getRejectedType();

    Integer getIsAlarm();

    Integer getCapacityAlarm();

    Integer getLivenessAlarm();

}
