package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.auth.model.biz.PermissionRespDTO;
import io.dynamic.threadpool.auth.service.PermissionService;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Permission controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/auth/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/{pageNo}/{pageSize}")
    public Result<IPage<PermissionRespDTO>> listPermission(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        IPage<PermissionRespDTO> resultPermissionPage = permissionService.listPermission(pageNo, pageSize);
        return Results.success(resultPermissionPage);
    }

    @PostMapping("/{role}/{resource}/{action}")
    public Result<Void> addPermission(@PathVariable("role") String role, @PathVariable("resource") String resource, @PathVariable("action") String action) {
        permissionService.addPermission(role, resource, action);
        return Results.success();
    }

    @DeleteMapping("/{role}/{resource}/{action}")
    public Result<Void> deleteUser(@PathVariable("role") String role, @PathVariable("resource") String resource, @PathVariable("action") String action) {
        permissionService.deletePermission(role, resource, action);
        return Results.success();
    }

}
