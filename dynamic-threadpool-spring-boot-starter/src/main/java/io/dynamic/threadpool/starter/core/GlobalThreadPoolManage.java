package io.dynamic.threadpool.starter.core;

import io.dynamic.threadpool.common.model.PoolParameter;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池全局管理
 */
public class GlobalThreadPoolManage {

    private static final Map<String, PoolParameter> POOL_PARAMETER = new ConcurrentHashMap();

    private static final Map<String, DynamicThreadPoolWrap> EXECUTOR_MAP = new ConcurrentHashMap();

    public static DynamicThreadPoolWrap getExecutorService(String tpId) {
        return EXECUTOR_MAP.get(tpId);
    }

    public static void register(String tpId, PoolParameter poolParameter, DynamicThreadPoolWrap executor) {
        registerPool(tpId, executor);
        registerPoolParameter(tpId, poolParameter);
    }

    public static void registerPool(String tpId, DynamicThreadPoolWrap executor) {
        EXECUTOR_MAP.put(tpId, executor);
    }

    public static void registerPoolParameter(String tpId, PoolParameter poolParameter) {
        POOL_PARAMETER.put(tpId, poolParameter);
    }

    public static void remove(String tpId) {
        EXECUTOR_MAP.remove(tpId);
    }

    public static PoolParameter getPoolParameter(String tpId) {
        return POOL_PARAMETER.get(tpId);
    }
}