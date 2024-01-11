package io.dynamic.threadpool.server.service.impl;

import io.dynamic.threadpool.server.mapper.RowMapperManager;
import io.dynamic.threadpool.server.model.ConfigAllInfo;
import io.dynamic.threadpool.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 服务端配置接口实现
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String namespace) {
        ConfigAllInfo configAllInfo = jdbcTemplate.queryForObject(
                "select * from config_info where tp_id = ? and item_id = ? and namespace = ?",
                new Object[]{tpId, itemId, namespace},
                RowMapperManager.CONFIG_ALL_INFO_ROW_MAPPER);

        return configAllInfo;
    }
}
