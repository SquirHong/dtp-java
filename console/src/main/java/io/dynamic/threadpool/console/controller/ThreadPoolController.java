package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolRespDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import io.dynamic.threadpool.config.service.biz.ThreadPoolService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Thread Pool Controller.
 */
@RestController
@RequestMapping(Constants.BASE_PATH + "/thread")
public class ThreadPoolController {

    @Autowired
    private ThreadPoolService threadPoolService;

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

}
