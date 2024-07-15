package io.dynamic.threadpool.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.auth.model.RoleInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Role mapper.
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleInfo> {
}
