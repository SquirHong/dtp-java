package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;

import io.dynamic.threadpool.config.model.biz.tenant.TenantQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantRespDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantSaveReqDTO;
import io.dynamic.threadpool.config.model.biz.tenant.TenantUpdateReqDTO;
import io.dynamic.threadpool.config.service.biz.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 业务控制器
 */
@RestController
@RequestMapping(Constants.BASE_PATH + "/tenant")
public class TenantController {
    @Autowired
    private TenantService tenantService;

    @PostMapping("/query/page")
    public Result<IPage<TenantRespDTO>> queryTenantPage(@RequestBody TenantQueryReqDTO reqDTO) {
        return Results.success(tenantService.queryTenantPage(reqDTO));
    }

    @GetMapping("/query/{tenantId}")
    public Result<TenantRespDTO> queryTenant(@PathVariable("tenantId") String tenantId) {
        return Results.success(tenantService.getTenantByTenantId(tenantId));
    }

    @PostMapping("/save")
    public Result saveTenant(@RequestBody TenantSaveReqDTO reqDTO) {
        tenantService.saveTenant(reqDTO);
        return Results.success();
    }

    @PostMapping("/update")
    public Result updateTenant(@RequestBody TenantUpdateReqDTO reqDTO) {
        tenantService.updateTenant(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/delete/{tenantId}")
    public Result deleteTenant(@PathVariable("tenantId") String tenantId) {
        tenantService.deleteTenantById(tenantId);
        return Results.success();
    }
}
