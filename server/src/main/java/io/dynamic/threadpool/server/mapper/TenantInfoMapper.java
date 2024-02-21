package io.dynamic.threadpool.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.server.model.TenantInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Tenant Info Mapper.
 */
@Mapper
public interface TenantInfoMapper extends BaseMapper<TenantInfo> {
}
