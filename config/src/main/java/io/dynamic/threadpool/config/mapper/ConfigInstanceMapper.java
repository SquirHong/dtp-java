package io.dynamic.threadpool.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.config.model.ConfigInstanceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Config instance mapper.
 */
@Mapper
public interface ConfigInstanceMapper extends BaseMapper<ConfigInstanceInfo> {
}
