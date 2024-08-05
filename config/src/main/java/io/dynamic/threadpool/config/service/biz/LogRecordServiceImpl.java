package io.dynamic.threadpool.config.service.biz;

import io.dynamic.threadpool.config.mapper.LogRecordMapper;
import io.dynamic.threadpool.logrecord.model.LogRecordInfo;
import io.dynamic.threadpool.logrecord.service.LogRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 操作日志保存数据库.
 */
@Service
@AllArgsConstructor
public class LogRecordServiceImpl implements LogRecordService {

    private final LogRecordMapper logRecordMapper;

    @Override
    public void record(LogRecordInfo logRecordInfo) {
        logRecordMapper.insert(logRecordInfo);
    }

}
