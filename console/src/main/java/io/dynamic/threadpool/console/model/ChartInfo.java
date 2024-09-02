package io.dynamic.threadpool.console.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Char info.
 */
@Data
@Accessors(chain = true)
public class ChartInfo {

    /**
     * 租户统计
     */
    private Integer tenantCount;

    /**
     * 项目统计
     */
    private Integer itemCount;

    /**
     * 线程池统计
     */
    private Integer threadPoolCount;

}
