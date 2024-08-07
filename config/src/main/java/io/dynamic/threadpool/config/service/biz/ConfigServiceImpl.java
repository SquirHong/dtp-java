package io.dynamic.threadpool.config.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.dynamic.threadpool.common.toolkit.ContentUtil;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.config.mapper.ConfigInfoMapper;
import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.service.ConfigChangePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务端配置接口实现
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigInfoMapper configInfoMapper;

    @Override
    public ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String tenantId) {
        LambdaQueryWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaQuery(ConfigAllInfo.class)
                .eq(!StringUtils.isBlank(tpId), ConfigAllInfo::getTpId, tpId)
                .eq(!StringUtils.isBlank(itemId), ConfigAllInfo::getItemId, itemId)
                .eq(!StringUtils.isBlank(tenantId), ConfigAllInfo::getTenantId, tenantId);
        ConfigAllInfo configAllInfo = configInfoMapper.selectOne(wrapper);
        return configAllInfo;
    }

    @Override
    public void insertOrUpdate(String identify, ConfigAllInfo configAllInfo) {
        try {
            addConfigInfo(configAllInfo);
            log.info("发布配置成功，即将发布LocalDataChangeEvent content :: {}", configAllInfo.getContent());
        } catch (Exception ex) {
            updateConfigInfo(configAllInfo);
            log.info("修改配置成功，即将发布LocalDataChangeEvent content :: {}", configAllInfo.getContent());
        }
        ConfigChangePublisher
                .notifyConfigChange(new LocalDataChangeEvent(identify, ContentUtil.getGroupKey(configAllInfo)));
    }

    private Integer addConfigInfo(ConfigAllInfo config) {
        config.setContent(ContentUtil.getPoolContent(config));
        config.setMd5(Md5Util.getTpContentMd5(config));
        try {
            if (SqlHelper.retBool(configInfoMapper.insert(config))) {
                return config.getId();
            }
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
        return null;
    }

    private void updateConfigInfo(ConfigAllInfo config) {
        LambdaUpdateWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaUpdate(ConfigAllInfo.class)
                .eq(ConfigAllInfo::getTpId, config.getTpId())
                .eq(ConfigAllInfo::getItemId, config.getItemId())
                .eq(ConfigAllInfo::getTenantId, config.getTenantId());

        config.setContent(ContentUtil.getPoolContent(config));
        config.setMd5(Md5Util.getTpContentMd5(config));
        try {
            configInfoMapper.update(config, wrapper);
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

}
