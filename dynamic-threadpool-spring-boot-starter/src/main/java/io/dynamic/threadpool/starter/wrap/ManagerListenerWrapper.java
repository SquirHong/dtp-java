package io.dynamic.threadpool.starter.wrap;

import io.dynamic.threadpool.starter.core.Listener;
import lombok.Getter;
import lombok.Setter;

/**
 * 监听包装
 */
@Getter
@Setter
public class ManagerListenerWrapper {

    Listener listener;

    String lastCallMd5;

    public ManagerListenerWrapper(String md5, Listener listener) {
        this.lastCallMd5 = md5;
        this.listener = listener;
    }
}
