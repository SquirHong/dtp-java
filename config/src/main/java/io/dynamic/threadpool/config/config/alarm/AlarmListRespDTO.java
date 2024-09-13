package io.dynamic.threadpool.config.config.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Alarm list resp dto.
 */
@Data
@AllArgsConstructor
public class AlarmListRespDTO {

    /**
     * 线程池ID
     */
    private String threadPoolId;

    /**
     * 报警配置
     */
    private List<AlarmInfo> alarmNotifyList;

}
