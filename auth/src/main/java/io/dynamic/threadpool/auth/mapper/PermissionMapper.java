package io.dynamic.threadpool.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.auth.model.PermissionInfo;
import org.apache.ibatis.annotations.Mapper;
/**
 * Permission mapper.
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionInfo> {
}
