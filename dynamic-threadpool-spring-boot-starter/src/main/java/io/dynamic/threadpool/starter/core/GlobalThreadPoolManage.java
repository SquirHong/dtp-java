package io.dynamic.threadpool.starter.core;

import io.dynamic.threadpool.common.model.PoolParameter;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrap;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池全局管理
 */
public class GlobalThreadPoolManage {

    private static final Map<String, PoolParameter> POOL_PARAMETER = new ConcurrentHashMap();

    private static final Map<String, DynamicThreadPoolWrap> EXECUTOR_MAP = new ConcurrentHashMap();

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

    public static DynamicThreadPoolWrap getExecutorService(String tpId) {
        return EXECUTOR_MAP.get(tpId);
    }

    public static PoolParameter getPoolParameter(String tpId) {
        return POOL_PARAMETER.get(tpId);
    }

    // 添加一个静态块或一个初始化方法来启动定时任务
//    static {
//        startPeriodicPrinting();
//    }

    private static void startPeriodicPrinting() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                printExecutorMap();
            }
        };

        Timer timer = new Timer("ExecutorMapPrinter");
        // 每隔5000毫秒（即5秒）执行一次
        timer.schedule(task, 30L, 5000L);
    }

    private static void printExecutorMap() {
        for (Map.Entry<String, DynamicThreadPoolWrap> entry : EXECUTOR_MAP.entrySet()) {
            System.out.println("Thread Pool ID: " + entry.getKey() + ", Executor: " + entry.getValue());
        }
    }
}