package io.dynamic.threadpool.logrecord.service;


import io.dynamic.threadpool.logrecord.model.LogRecordInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认实现日志存储.
 */
@Slf4j
public class DefaultLogRecordServiceImpl implements LogRecordService {

    @Override
    public void record(LogRecordInfo logRecordInfo) {
        log.info("Log print :: {}", logRecordInfo);
    }

}
