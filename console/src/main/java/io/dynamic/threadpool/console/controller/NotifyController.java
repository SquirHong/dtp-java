package io.dynamic.threadpool.console.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.model.biz.notify.NotifyListRespDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyModifyReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyRespDTO;
import io.dynamic.threadpool.config.service.biz.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/notify")
public class NotifyController {

    private final NotifyService notifyService;

    @PostMapping("/list/config")
    public Result<List<NotifyListRespDTO>> listAlarmConfig(@RequestBody NotifyQueryReqDTO reqDTO) {
        List<NotifyListRespDTO> resultData = notifyService.listNotifyConfig(reqDTO);
        return Results.success(resultData);
    }

    @PostMapping("/query/page")
    public Result<IPage<NotifyRespDTO>> queryPage(@RequestBody NotifyQueryReqDTO reqDTO) {
        IPage<NotifyRespDTO> resultPage = notifyService.queryPage(reqDTO);
        return Results.success(resultPage);
    }

    @PostMapping("/save")
    public Result<Void> saveNotifyConfig(@RequestBody NotifyModifyReqDTO reqDTO) {
        notifyService.save(reqDTO);
        return Results.success();
    }

    @PostMapping("/update")
    public Result<Void> updateNotifyConfig(@RequestBody NotifyModifyReqDTO reqDTO) {
        notifyService.update(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/remove")
    public Result<Void> removeNotifyConfig(@RequestBody NotifyModifyReqDTO reqDTO) {
        notifyService.delete(reqDTO);
        return Results.success();
    }

    @PostMapping("/enable/{id}/{status}")
    public Result enableNotify(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        notifyService.enableNotify(id, status);
        return Results.success();
    }

}
