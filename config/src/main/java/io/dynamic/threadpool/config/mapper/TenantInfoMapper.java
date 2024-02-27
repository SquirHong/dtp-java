package io.dynamic.threadpool.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.config.model.TenantInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Tenant Info Mapper.
 */
@Mapper
public interface TenantInfoMapper extends BaseMapper<TenantInfo> {
}
