package io.dynamic.threadpool.config.service.biz;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.dynamic.threadpool.common.toolkit.ContentUtil;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.common.toolkit.UserContext;
import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.mapper.ConfigInfoMapper;
import io.dynamic.threadpool.config.mapper.ConfigInstanceMapper;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.model.ConfigInfoBase;
import io.dynamic.threadpool.config.model.ConfigInstanceInfo;
import io.dynamic.threadpool.config.service.ConfigChangePublisher;
import io.dynamic.threadpool.config.toolkit.BeanUtil;
import io.dynamic.threadpool.logrecord.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务端配置接口实现
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigInfoMapper configInfoMapper;

    @Resource
    private ConfigInstanceMapper configInstanceMapper;

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
    public ConfigAllInfo findConfigRecentInfo(String... params) {
        log.info("开始打印findConfigRecentInfo参数");
        for (String param : params) {
            log.info("param :: {}", param);
        }
        ConfigAllInfo resultConfig;
        ConfigAllInfo configInstance = null;
        LambdaQueryWrapper<ConfigInstanceInfo> instanceQueryWrapper = Wrappers.lambdaQuery(ConfigInstanceInfo.class)
                .eq(ConfigInstanceInfo::getInstanceId, params[3])
                .orderByDesc(ConfigInstanceInfo::getGmtModified)
                .last("LIMIT 1");
        ConfigInstanceInfo instanceInfo = configInstanceMapper.selectOne(instanceQueryWrapper);

        if (instanceInfo != null) {
            String content = instanceInfo.getContent();
            configInstance = JSON.parseObject(content, ConfigAllInfo.class);
            configInstance.setContent(content);
            configInstance.setGmtCreate(instanceInfo.getGmtCreate());
            configInstance.setGmtModified(instanceInfo.getGmtModified());
            configInstance.setMd5(Md5Util.getTpContentMd5(configInstance));
        }

        ConfigAllInfo configAllInfo = findConfigAllInfo(params[0], params[1], params[2]);
        if (configAllInfo != null && configInstance == null) {
            resultConfig = configAllInfo;
        } else if (configAllInfo == null && configInstance != null) {
            resultConfig = configInstance;
        } else {
            if (configAllInfo.getGmtModified().before(configInstance.getGmtModified())) {
                resultConfig = configInstance;
            } else {
                resultConfig = configAllInfo;
            }
        }

        return resultConfig;

    }

    @Override
    public void insertOrUpdate(String identify, ConfigAllInfo configAllInfo) {
        LambdaQueryWrapper<ConfigAllInfo> queryWrapper = Wrappers.lambdaQuery(ConfigAllInfo.class)
                .eq(ConfigInfoBase::getTenantId, configAllInfo.getTenantId())
                .eq(ConfigInfoBase::getItemId, configAllInfo.getItemId())
                .eq(ConfigInfoBase::getTpId, configAllInfo.getTpId());
        ConfigAllInfo existConfig = configInfoMapper.selectOne(queryWrapper);

        // 做LogRecord
        String userName = UserContext.getUserName();

        configAllInfo.setCapacity(getQueueCapacityByType(configAllInfo));


        try {
            if (existConfig == null) {
                addConfigInfo(configAllInfo);
                log.info("发布配置成功，即将发布LocalDataChangeEvent identify::{} content :: {}", identify, configAllInfo.getContent());
            } else {
                updateConfigInfo(identify, userName, configAllInfo);
                log.info("修改配置成功，即将发布LocalDataChangeEvent identify::{} content :: {}", identify, configAllInfo.getContent());
            }
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
        ConfigChangePublisher
                .notifyConfigChange(new LocalDataChangeEvent(identify, ContentUtil.getGroupKey(configAllInfo)));
    }

    private Long addConfigInfo(ConfigAllInfo config) {
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

    @LogRecord(
            bizNo = "{{#config.itemId}}_{{#config.tpId}}",
            category = "THREAD_POOL_UPDATE",
            operator = "{{#operator}}",
            success = "核心线程: {{#config.coreSize}}, 最大线程: {{#config.maxSize}}, 队列类型: {{#config.queueType}}, 队列容量: {{#config.capacity}}, 拒绝策略: {{#config.rejectedType}}",
            detail = "{{#config.toString()}}"
    )
    public void updateConfigInfo(String identify, String operator, ConfigAllInfo config) {
        LambdaUpdateWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaUpdate(ConfigAllInfo.class)
                .eq(ConfigAllInfo::getTpId, config.getTpId())
                .eq(ConfigAllInfo::getItemId, config.getItemId())
                .eq(ConfigAllInfo::getTenantId, config.getTenantId());

        config.setContent(ContentUtil.getPoolContent(config));
        config.setMd5(Md5Util.getTpContentMd5(config));
        try {
            // 创建线程池配置实例 临时!配置，也可以当作历史配置，不过针对的是单节点
            if (StrUtil.isNotBlank(identify)) {
                ConfigInstanceInfo instanceInfo = BeanUtil.convert(config, ConfigInstanceInfo.class);
                // 特殊处理，BeanUtil.convert 会将 配置的create和modified时间也转换过去
                instanceInfo.setGmtCreate(null);
                instanceInfo.setGmtModified(null);
                instanceInfo.setInstanceId(identify);
                configInstanceMapper.insert(instanceInfo);
                log.info("新增差异化配置成功，instanceInfo :: {}", JSON.toJSONString(instanceInfo));
                return;
            }

            configInfoMapper.update(config, wrapper);
        } catch (Exception ex) {
            log.error("[db-error] message :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * 根据队列类型获取队列大小.
     * <p>
     * 不支持设置队列大小 {@link SynchronousQueue} {@link LinkedTransferQueue}
     * 前者为0，后者为无界
     *
     * @param config
     * @return
     */
    private Integer getQueueCapacityByType(ConfigAllInfo config) {
        int queueCapacity;
        switch (config.getQueueType()) {
            case 5:
                queueCapacity = Integer.MAX_VALUE;
                break;
            default:
                queueCapacity = config.getCapacity();
                break;
        }

        List<Integer> queueTypes = Stream.of(1, 2, 3, 6, 9).collect(Collectors.toList());
        boolean setDefaultFlag = queueTypes.contains(config.getQueueType()) && (config.getCapacity() == null || Objects.equals(config.getCapacity(), 0));
        if (setDefaultFlag) {
            queueCapacity = 1024;
        }

        return queueCapacity;
    }

}
