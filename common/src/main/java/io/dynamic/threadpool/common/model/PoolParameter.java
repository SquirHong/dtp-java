package io.dynamic.threadpool.common.model;

/**
 * Pool Parameter.
 */
public interface PoolParameter {

    String getNamespace();

    String getItemId();

    String getTpId();

    Integer getCoreSize();

    Integer getMaxSize();

    Integer getQueueType();

    Integer getCapacity();

    Integer getKeepAliveTime();

    Integer getIsAlarm();

    Integer getCapacityAlarm();

    Integer getLivenessAlarm();

}
