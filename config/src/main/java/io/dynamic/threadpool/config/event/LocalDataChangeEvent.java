package io.dynamic.threadpool.config.event;

/**
 * Local Data Change Event.处理客户端和服务端之间的长连接的subs，告诉客户端哪个配置被修改了
 */
public class LocalDataChangeEvent extends Event {

    /**
     * 租户+项目+线程池
     */
    public final String groupKey;

    /**
     * 客户端实例唯一标识
     */
    public final String identify;

    public LocalDataChangeEvent(String identify, String groupKey) {
        this.identify = identify;
        this.groupKey = groupKey;
    }
}
