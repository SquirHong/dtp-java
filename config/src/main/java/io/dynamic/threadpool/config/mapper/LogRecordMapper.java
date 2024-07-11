package io.dynamic.threadpool.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.logrecord.model.LogRecordInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Log record mapper.
 */
@Mapper
public interface LogRecordMapper extends BaseMapper<LogRecordInfo> {
}
