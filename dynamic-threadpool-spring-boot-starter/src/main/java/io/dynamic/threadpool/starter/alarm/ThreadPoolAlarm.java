package io.dynamic.threadpool.starter.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadPoolAlarm {

    /**
     * 是否报警
     */
    private Boolean isAlarm;

    /**
     * 活跃度报警
     */
    private Integer livenessAlarm;

    /**
     * 容量报警
     */
    private Integer capacityAlarm;

}
