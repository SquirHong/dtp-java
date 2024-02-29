package io.dynamic.threadpool.common.executor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread Pool Manager.
 */
public class ThreadPoolManager {

    // tenantId 、group 、executors
    private Map<String, Map<String, Set<ExecutorService>>> resourcesManager;

    private Map<String, Object> lockers = new ConcurrentHashMap(8);

    private static final AtomicBoolean CLOSED = new AtomicBoolean(false);

    private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE.init();
    }

    private void init() {
        resourcesManager = new ConcurrentHashMap<String, Map<String, Set<ExecutorService>>>();
    }

    public void register(String tenantId, String group, ExecutorService executor) {
        if (!resourcesManager.containsKey(tenantId)) {
            synchronized (this) {
                // 细分注册的步骤
                lockers.put(tenantId, new Object());
            }
        }
        final Object monitor = lockers.get(tenantId);
        // 真正开始锁
        synchronized (monitor) {
            Map<String, Set<ExecutorService>> map = resourcesManager.get(tenantId);
            if (map == null) {
                map = new HashMap(8);
                map.put(group, new HashSet());
                map.get(group).add(executor);
                resourcesManager.put(tenantId, map);
                return;
            }
            if (!map.containsKey(group)) {
                map.put(group, new HashSet());
            }
            map.get(group).add(executor);
        }
    }
}
