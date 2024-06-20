package io.dynamic.threadpool.registry.controller;

import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.common.web.exception.ErrorCodeEnum;
import io.dynamic.threadpool.registry.core.InstanceRegistry;
import io.dynamic.threadpool.registry.core.Lease;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dynamic.threadpool.common.constant.Constants.BASE_PATH;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_PATH + "/apps")
public class ApplicationController {

    @NonNull
    private final InstanceRegistry instanceRegistry;

    @GetMapping("/{appName}")
    public Result applications(@PathVariable String appName) {
        List<Lease<InstanceInfo>> resultInstanceList = instanceRegistry.listInstance(appName);
        return Results.success(resultInstanceList);
    }

    @PostMapping("/register")
    public Result addInstance(@RequestBody InstanceInfo instanceInfo) {
        instanceRegistry.register(instanceInfo);
        return Results.success();
    }

    @PostMapping("/renew")
    public Result renew(@RequestBody InstanceInfo.InstanceRenew instanceRenew) {
        boolean isSuccess = instanceRegistry.renew(instanceRenew);
        if (!isSuccess) {
            log.warn("Not Found (Renew) :: {} - {}", instanceRenew.getAppName(), instanceRenew.getInstanceId());
            return Results.failure(ErrorCodeEnum.NOT_FOUND);
        }
        return Results.success();
    }

}
