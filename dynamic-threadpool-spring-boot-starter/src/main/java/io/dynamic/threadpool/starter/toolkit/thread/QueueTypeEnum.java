package io.dynamic.threadpool.starter.toolkit.thread;

import io.dynamic.threadpool.starter.spi.DynamicThreadPoolServiceLoader;
import io.dynamic.threadpool.starter.spi.CustomBlockingQueue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * 队列类型枚举
 *
 * @author chen.ma
 * @date 2021/6/25 12:30
 */
public enum QueueTypeEnum {

    /**
     * {@link java.util.concurrent.ArrayBlockingQueue}
     */
    ARRAY_BLOCKING_QUEUE(1, "ArrayBlockingQueue"),

    /**
     * {@link java.util.concurrent.LinkedBlockingQueue}
     */
    LINKED_BLOCKING_QUEUE(2, "LinkedBlockingQueue"),

    /**
     * {@link java.util.concurrent.LinkedBlockingDeque}
     */
    LINKED_BLOCKING_DEQUE(3, "LinkedBlockingDeque"),

    /**
     * {@link java.util.concurrent.SynchronousQueue}
     */
    SYNCHRONOUS_QUEUE(4, "SynchronousQueue"),

    /**
     * {@link java.util.concurrent.LinkedTransferQueue}
     */
    LINKED_TRANSFER_QUEUE(5, "LinkedTransferQueue"),

    /**
     * {@link java.util.concurrent.PriorityBlockingQueue}
     */
    PRIORITY_BLOCKING_QUEUE(6, "PriorityBlockingQueue"),

    /**
     * {@link "io.dynamic.threadpool.starter.toolkit.thread.ResizableCapacityLinkedBlockIngQueue"}
     */
    RESIZABLE_LINKED_BLOCKING_QUEUE(9, "ResizableCapacityLinkedBlockIngQueue");


    public Integer type;

    public String name;

    QueueTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    static {
        DynamicThreadPoolServiceLoader.register(CustomBlockingQueue.class);
    }

    public static BlockingQueue createBlockingQueue(Integer type, Integer capacity) {
        BlockingQueue blockingQueue = null;
        if (Objects.equals(type, ARRAY_BLOCKING_QUEUE.type)) {
            blockingQueue = new ArrayBlockingQueue(capacity);
        } else if (Objects.equals(type, LINKED_BLOCKING_QUEUE.type)) {
            blockingQueue = new LinkedBlockingQueue(capacity);
        } else if (Objects.equals(type, LINKED_BLOCKING_DEQUE.type)) {
            blockingQueue = new LinkedBlockingDeque(capacity);
        } else if (Objects.equals(type, SYNCHRONOUS_QUEUE.type)) {
            blockingQueue = new SynchronousQueue();
        } else if (Objects.equals(type, LINKED_TRANSFER_QUEUE.type)) {
            blockingQueue = new LinkedTransferQueue();
        } else if (Objects.equals(type, PRIORITY_BLOCKING_QUEUE.type)) {
            blockingQueue = new PriorityBlockingQueue(capacity);
        } else if (Objects.equals(type, RESIZABLE_LINKED_BLOCKING_QUEUE.type)) {
            blockingQueue = new ResizableCapacityLinkedBlockIngQueue(capacity);
        }

        Collection<CustomBlockingQueue> customBlockingQueues = DynamicThreadPoolServiceLoader
                .getSingletonServiceInstances(CustomBlockingQueue.class);
        blockingQueue = Optional.ofNullable(blockingQueue).orElseGet(() -> customBlockingQueues.stream()
                .filter(each -> Objects.equals(type, each.getType()))
                .map(each -> each.generateBlockingQueue())
                .findFirst()
                .orElse(new LinkedBlockingQueue(capacity)));

        return blockingQueue;
    }

    public static String getBlockingQueueNameByType(Integer type) {
        Optional<QueueTypeEnum> queueTypeEnum = Arrays.stream(QueueTypeEnum.values())
                .filter(each -> each.type == type)
                .findFirst();

        return queueTypeEnum.map(each -> each.name).orElse("");
    }

}
