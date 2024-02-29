package io.dynamic.threadpool.config.controller;

import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;

import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.model.ConfigInfoBase;
import io.dynamic.threadpool.config.service.ConfigServletInner;
import io.dynamic.threadpool.config.service.biz.ConfigService;
import io.dynamic.threadpool.config.toolkit.Md5ConfigUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 服务端配置控制器
 */
@Slf4j
@RestController
@RequestMapping(Constants.CONFIG_CONTROLLER_PATH)
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigServletInner configServletInner;

    /**
     * 获取指定线程池配置
     */
    @GetMapping
    public Result<ConfigInfoBase> detailConfigInfo(@RequestParam("tpId") String tpId, @RequestParam("itemId") String itemId, @RequestParam(value = "tenantId") String tenantId) {

        return Results.success(configService.findConfigAllInfo(tpId, itemId, tenantId));
    }

    @PostMapping
    public Result<Boolean> publishConfig(@RequestBody ConfigAllInfo config) {
        configService.insertOrUpdate(config);
        return Results.success(true);
    }

    @SneakyThrows
    @PostMapping("/listener")
    public void listener(HttpServletRequest request, HttpServletResponse response) {
        log.debug("request : {}", request);
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        String probeModify = request.getParameter(Constants.LISTENING_CONFIGS);
        if (StringUtils.isEmpty(probeModify)) {
            throw new IllegalArgumentException("invalid probeModify");
        }

        probeModify = URLDecoder.decode(probeModify, Constants.ENCODE);

        Map<String, String> clientMd5Map;
        try {
            clientMd5Map = Md5ConfigUtil.getClientMd5Map(probeModify);
        } catch (Throwable e) {
            throw new IllegalArgumentException("invalid probeModify");
        }

        configServletInner.doPollingConfig(request, response, clientMd5Map, probeModify.length());
    }

}
