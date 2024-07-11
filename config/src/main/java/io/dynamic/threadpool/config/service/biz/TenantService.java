package io.dynamic.threadpool.config.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.config.model.biz.tenant.TenantQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantRespDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantSaveReqDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantUpdateReqDTO;

/**
 * 业务接口
 */
public interface TenantService {

    /**
     * Get tenant by id.
     *
     * @param id
     * @return
     */
    TenantRespDTO getTenantById(String id);

    /**
     * Get tenant by tenantId.
     *
     * @param tenantId
     * @return
     */
    TenantRespDTO getTenantByTenantId(String tenantId);

    /**
     * 分页查询租户
     *
     * @param reqDTO
     * @return
     */
    IPage<TenantRespDTO> queryTenantPage(TenantQueryReqDTO reqDTO);

    /**
     * 新增租户
     *
     * @param reqDTO
     */
    void saveTenant(TenantSaveReqDTO reqDTO);

    /**
     * 修改租户
     *
     * @param reqDTO
     */
    void updateTenant(TenantUpdateReqDTO reqDTO);

    /**
     * 根据 Id 删除租户
     *
     * @param tenantId
     */
    void deleteTenantById(String tenantId);
}
