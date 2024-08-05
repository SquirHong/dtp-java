package io.dynamic.threadpool.config.service;


import io.dynamic.threadpool.config.model.biz.tenant.TenantRespDTO;
import io.dynamic.threadpool.config.service.biz.TenantService;
import io.dynamic.threadpool.logrecord.service.ParseFunction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 查找项目 Id 旧值.
 */
@Component
@AllArgsConstructor
public class TenantIdFunctionServiceImpl implements ParseFunction {

    private final TenantService tenantService;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "TENANT";
    }

    @Override
    public String apply(String tenantId) {
        TenantRespDTO tenant = tenantService.getTenantById(tenantId);
        return Optional.ofNullable(tenant).map(TenantRespDTO::getTenantName).orElse("");
    }

}
