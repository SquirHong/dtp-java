package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.model.InstanceInfo;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolRespDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import io.dynamic.threadpool.config.service.biz.ThreadPoolService;
import io.dynamic.threadpool.registry.core.BaseInstanceRegistry;
import io.dynamic.threadpool.registry.core.Lease;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Thread Pool Controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/thread")
public class ThreadPoolController {

    private final ThreadPoolService threadPoolService;

    private final BaseInstanceRegistry baseInstanceRegistry;

    @PostMapping("/pool/query/page")
    public Result<IPage<ThreadPoolRespDTO>> queryNameSpacePage(@RequestBody ThreadPoolQueryReqDTO reqDTO) {
        return Results.success(threadPoolService.queryThreadPoolPage(reqDTO));
    }

    @PostMapping("/pool/query")
    public Result<ThreadPoolRespDTO> queryNameSpace(@RequestBody ThreadPoolQueryReqDTO reqDTO) {
        return Results.success(threadPoolService.getThreadPool(reqDTO));
    }

    @PostMapping("/pool/save_or_update")
    public Result saveOrUpdateThreadPoolConfig(@RequestBody ThreadPoolSaveOrUpdateReqDTO reqDTO) {
        threadPoolService.saveOrUpdateThreadPoolConfig(reqDTO);
        return Results.success();
    }

    @GetMapping("/pool/list/instance/{itemId}")
    public Result<List<Lease<InstanceInfo>>> listInstance(@PathVariable("itemId") String itemId) {
        List<Lease<InstanceInfo>> leases = baseInstanceRegistry.listInstance(itemId);
        return Results.success(leases);
    }

}
