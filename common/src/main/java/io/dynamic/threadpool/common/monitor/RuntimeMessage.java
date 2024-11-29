package io.dynamic.threadpool.common.monitor;

import lombok.Data;


@Data
public class RuntimeMessage extends AbstractMessage {

    /**
     * currentLoad
     */
    private String currentLoad;

    /**
     * peakLoad
     */
    private String peakLoad;

    /**
     * poolSize
     */
    private Integer poolSize;

    /**
     * The maximum number of threads that enter the thread pool at the same time
     */
    private Integer largestPoolSize;

    /**
     * queueCapacity
     */
    private Integer queueCapacity;

    /**
     * queueSize
     */
    private Integer queueSize;

    /**
     * completedTaskCount
     */
    private Long completedTaskCount;

    /**
     * rejectCount
     */
    private Integer rejectCount;

}
