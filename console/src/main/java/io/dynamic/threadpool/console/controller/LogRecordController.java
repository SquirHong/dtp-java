package io.dynamic.threadpool.console.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.model.biz.log.LogRecordQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.log.LogRecordRespDTO;
import io.dynamic.threadpool.config.service.biz.LogRecordBizService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Log record controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/log")
public class LogRecordController {

    private final LogRecordBizService logRecordBizService;

    @PostMapping("/query/page")
    public Result<IPage<LogRecordRespDTO>> queryPage(@RequestBody LogRecordQueryReqDTO queryReqDTO) {
        return Results.success(logRecordBizService.queryPage(queryReqDTO));
    }

}
