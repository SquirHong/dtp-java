package io.dynamic.threadpool.logrecord.service;

import io.dynamic.threadpool.logrecord.model.LogRecordInfo;

/**
 * 日志记录.
 */
public interface LogRecordService {

    /**
     * 保存日志.
     *
     * @param logRecordInfo
     */
    void record(LogRecordInfo logRecordInfo);

}
