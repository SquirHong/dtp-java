package io.dynamic.threadpool.starter.controller;

import io.dynamic.threadpool.common.model.PoolRunStateInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.starter.handle.ThreadPoolRunStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Pool Run State Controller.
 */
@RestController
public class PoolRunStateController {

    @Autowired
    private ThreadPoolRunStateHandler threadPoolRunStateHandler;

    @GetMapping("/run/state/{tpId}")
    public Result<PoolRunStateInfo> getPoolRunState(@PathVariable("tpId") String tpId) {
        PoolRunStateInfo poolRunState = threadPoolRunStateHandler.getPoolRunState(tpId);
        return Results.success(poolRunState);
    }
}
