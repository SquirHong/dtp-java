package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.auth.model.biz.RoleRespDTO;
import io.dynamic.threadpool.auth.service.RoleService;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v1/auth/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{pageNo}/{pageSize}")
    public Result<IPage<RoleRespDTO>> listUser(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        IPage<RoleRespDTO> resultRolePage = roleService.listRole(pageNo, pageSize);
        return Results.success(resultRolePage);
    }

    @PostMapping("/{role}/{userName}")
    public Result<Void> addUser(@PathVariable("role") String role, @PathVariable("userName") String userName) {
        roleService.addRole(role, userName);
        return Results.success();
    }

    @DeleteMapping("/{role}/{userName}")
    public Result<Void> deleteUser(@PathVariable("role") String role, @PathVariable("userName") String userName) {
        roleService.deleteRole(role, userName);
        return Results.success();
    }

    @GetMapping("/search/{role}")
    public Result<List<String>> searchUsersLikeUserName(@PathVariable("role") String role) {
        List<String> resultRole = roleService.getRoleLike(role);
        return Results.success(resultRole);
    }

}
