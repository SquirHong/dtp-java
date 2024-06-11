package io.dynamic.threadpool.starter.core;

import io.dynamic.threadpool.common.toolkit.ContentUtil;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.starter.wrap.ManagerListenerWrap;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CacheData.
 */
@Slf4j
public class CacheData {

    public volatile String md5;

    public volatile String content;

    public final String tenantId;

    public final String itemId;

    public final String tpId;

    private int taskId;

    private volatile long localConfigLastModified;

    private final CopyOnWriteArrayList<ManagerListenerWrap> listeners;

    public CacheData(String tenantId, String itemId, String tpId) {
        this.tenantId = tenantId;
        this.itemId = itemId;
        this.tpId = tpId;
        // TODO：nacos 走的本地文件获取, 这里思考下如何优雅获取
        // todo：nacos原逻辑是考虑到宕机重启会再次为CacheMap添加CacheData，而content就可以 复用！ 先前的 failover和Snapshot文件
        // todo：当前逻辑为在 order1024处 为GlobalThreadPoolManage添加content，这里再获取，这里是order1025
        this.content = ContentUtil.getPoolContent(GlobalThreadPoolManage.getPoolParameter(tpId));
        this.md5 = getMd5String(content);
        this.listeners = new CopyOnWriteArrayList();

    }

    /**
     * 添加监听器
     */
    public void addListener(Listener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("listener is null");
        }

        ManagerListenerWrap managerListenerWrap = new ManagerListenerWrap(md5, listener);

        if (listeners.addIfAbsent(managerListenerWrap)) {
            // 添加成功
            log.info("[add-listener] ok, tpId :: {}, cnt :: {}", tpId, listeners.size());
        }
    }

    /**
     * 检查监听器的唯一md5值，如不同就是有 修改配置 的命令
     */
    public void checkListenerMd5() {
        for (ManagerListenerWrap wrap : listeners) {
            if (!md5.equals(wrap.getLastCallMd5())) {
                safeNotifyListener(content, md5, wrap);
            }
        }
    }

    /**
     * 将content和md5传递给Listener对象，并通过执行器异步地执行receiveConfigInfo方法。
     */
    private void safeNotifyListener(String content, String md5, ManagerListenerWrap wrap) {
        Listener listener = wrap.getListener();

        Runnable runnable = () -> {
            wrap.setLastCallMd5(md5);
            listener.receiveConfigInfo(content);
        };

        listener.getExecutor().execute(runnable);
    }

    public void setContent(String content) {
        this.content = content;
        this.md5 = getMd5String(this.content);
    }

    public static String getMd5String(String config) {
        return (null == config) ? Constants.NULL : Md5Util.md5Hex(config, Constants.ENCODE);
    }

    public String getMd5() {
        return this.md5;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
