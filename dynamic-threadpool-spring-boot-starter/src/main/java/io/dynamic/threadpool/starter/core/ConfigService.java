package io.dynamic.threadpool.starter.core;

import org.springframework.stereotype.Component;

/**
 * 配置服务
 */
@Component
public interface ConfigService {

    /**
     * 添加监听器, 如果服务端发生变更, 客户端会使用监听器进行回调
     *
     * @param tenantId
     * @param itemId
     * @param tpId
     * @param listener
     */
    void addListener(String tenantId, String itemId, String tpId, Listener listener);

    /**
     * 获取服务状态
     *
     * @return
     */
    String getServerStatus();

}
