package io.dynamic.threadpool.config.event;

/**
 * Local Data Change Event.处理客户端和服务端之间的长连接的subs，告诉客户端哪个配置被修改了
 */
public class LocalDataChangeEvent extends Event {

    public final String groupKey;

    public LocalDataChangeEvent(String groupKey) {
        this.groupKey = groupKey;
    }
}
