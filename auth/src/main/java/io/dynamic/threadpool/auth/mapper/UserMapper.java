package io.dynamic.threadpool.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dynamic.threadpool.auth.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * User mapper.
 */
@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
