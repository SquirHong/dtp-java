package io.dynamic.threadpool.console.controller;


import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.console.model.ChartInfo;
import io.dynamic.threadpool.console.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dash board controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public Result<ChartInfo> dashboard() {
        return Results.success(dashboardService.getChartInfo());
    }

}
