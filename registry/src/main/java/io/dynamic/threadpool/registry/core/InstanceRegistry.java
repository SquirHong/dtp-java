package io.dynamic.threadpool.registry.core;


import io.dynamic.threadpool.common.model.InstanceInfo;

import java.util.List;

public interface InstanceRegistry<T> {

    /**
     * list Instance.
     *
     * @param appName
     * @return
     */
    List<Lease<T>> listInstance(String appName);

    /**
     * register.
     *
     * @param info
     */
    void register(T info);

    /**
     * renew.
     *
     * @param instanceRenew
     * @return
     */
    boolean renew(InstanceInfo.InstanceRenew instanceRenew);

    /**
     * Remove.
     *
     * @param info
     */
    void remove(T info);

}
