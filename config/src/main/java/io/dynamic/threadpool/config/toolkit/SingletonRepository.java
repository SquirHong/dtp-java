package io.dynamic.threadpool.config.toolkit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Repository.
 */
public class SingletonRepository<T> {

    private final ConcurrentHashMap<T, T> shared;

    public SingletonRepository() {
        // 初始化大小 2^14，容器本身使用大约 50K 的内存，避免不断扩展
        shared = new ConcurrentHashMap(1 << 14);
    }

    /**
     * 已存在相同的对象，则返回已存在的对象。
     * 不存在相同的对象，则将存入obj，并返回obj。
     */
    public T getSingleton(T obj) {
        T previous = shared.putIfAbsent(obj, obj);
        return (null == previous) ? obj : previous;
    }

    public int size() {
        return shared.size();
    }

    public void remove(Object obj) {
        shared.remove(obj);
    }


    /**
     * Cache of DataId and Group.
     */
    public static class DataIdGroupIdCache {

        static SingletonRepository<String> cache = new SingletonRepository();

        public static String getSingleton(String str) {
            return cache.getSingleton(str);
        }

    }
}
