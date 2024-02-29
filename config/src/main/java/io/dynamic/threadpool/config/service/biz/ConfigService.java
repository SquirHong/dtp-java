package io.dynamic.threadpool.config.service.biz;

import io.dynamic.threadpool.config.model.ConfigAllInfo;

/**
 * 服务端配置接口
 */
public interface ConfigService {

    /**
     * 查询配置全部信息
     *
     * @param tpId    tpId
     * @param itemId itemId
     * @param tenantId  tenantId
     * @return 全部配置信息
     */
    ConfigAllInfo findConfigAllInfo(String tpId, String itemId, String tenantId);


    /**
     * 新增或修改
     *
     * @param configAllInfo
     */
    void insertOrUpdate(ConfigAllInfo configAllInfo);
}