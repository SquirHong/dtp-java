package io.dynamic.threadpool.starter.wrap;

import io.dynamic.threadpool.starter.listener.Listener;
import lombok.Getter;
import lombok.Setter;

/**
 * 监听包装
 */
@Getter
@Setter
public class ManagerListenerWrap {

    Listener listener;

    String lastCallMd5;

    public ManagerListenerWrap(String md5, Listener listener) {
        this.lastCallMd5 = md5;
        this.listener = listener;
    }
}
