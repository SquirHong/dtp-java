package io.dynamic.threadpool.logrecord.model;

import lombok.Builder;
import lombok.Data;

/**
 * 日志操作记录.
 */
@Data
@Builder
public class LogRecordOps {

    private String successLogTemplate;

    private String failLogTemplate;

    private String operatorId;

    private String bizKey;

    private String bizNo;

    private String category;

    private String detail;

    private String condition;

}
