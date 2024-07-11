package io.dynamic.threadpool.starter.toolkit.thread;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可调整大小的阻塞队列
 */
@Slf4j
public class ResizableCapacityLinkedBlockIngQueue<E> extends LinkedBlockingQueue<E> {

    public ResizableCapacityLinkedBlockIngQueue(int capacity) {
        super(capacity);
    }

    /**
     * 修改队列大小
     */
    public synchronized boolean setCapacity(Integer capacity) {
        boolean successFlag = true;
        try {
            int oldCapacity = (int) ReflectUtil.getFieldValue(this, "capacity");
            AtomicInteger count = (AtomicInteger) ReflectUtil.getFieldValue(this, "count");
            int size = count.get();

            ReflectUtil.setFieldValue(this, "capacity", capacity);

            if (capacity > size && size >= oldCapacity) {
                ReflectUtil.invoke(this, "signalNotFull");
            }
        } catch (Exception ex) {
            // ignore
            log.error("动态修改阻塞队列大小失败.", ex);
            successFlag = false;
        }
        return successFlag;
    }
}
