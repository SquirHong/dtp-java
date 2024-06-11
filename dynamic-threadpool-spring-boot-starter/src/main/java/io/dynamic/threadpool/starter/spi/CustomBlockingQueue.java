package io.dynamic.threadpool.starter.spi;

import java.util.concurrent.BlockingQueue;

/**
 * 自定义阻塞队列
 */
public interface CustomBlockingQueue {

    /**
     * 获取类型
     *
     * @return
     */
    Integer getType();

    /**
     * 生成阻塞队列
     *
     * @return
     */
    BlockingQueue generateBlockingQueue();

}
