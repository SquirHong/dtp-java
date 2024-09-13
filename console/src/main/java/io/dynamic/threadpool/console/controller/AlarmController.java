package io.dynamic.threadpool.console.controller;


import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.config.alarm.AlarmListRespDTO;
import io.dynamic.threadpool.config.config.alarm.AlarmQueryReqDTO;
import io.dynamic.threadpool.config.config.alarm.AlarmService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警管理.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/list/config")
    public Result<List<AlarmListRespDTO>> listAlarmConfig(@RequestBody AlarmQueryReqDTO reqDTO) {
        List<AlarmListRespDTO> resultData = alarmService.listAlarmConfig(reqDTO);
        return Results.success(resultData);
    }

}
