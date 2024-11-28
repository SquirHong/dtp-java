package io.dynamic.threadpool.config.controller;

import cn.hutool.core.util.StrUtil;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;

import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.model.ConfigInfoBase;
import io.dynamic.threadpool.config.service.ConfigCacheService;
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
import java.util.stream.Collectors;

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
    public Result<ConfigInfoBase> detailConfigInfo(@RequestParam("tpId") String tpId,
                                                   @RequestParam("itemId") String itemId,
                                                   @RequestParam(value = "tenantId") String tenantId,
                                                   @RequestParam(value = "instanceId", required = false) String instanceId) {

        ConfigAllInfo configAllInfo = configService.findConfigRecentInfo(tpId, itemId, tenantId, instanceId);
        return Results.success(configAllInfo);
    }

    @PostMapping
    public Result<Boolean> publishConfig(@RequestParam(value = "identify", required = false) String identify,
                                         @RequestBody ConfigAllInfo config) {
        configService.insertOrUpdate(identify, config);
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
            String mapAsString = clientMd5Map.entrySet().stream().map(entry -> "Key: " + entry.getKey() + ", Value: " + entry.getValue())
                    .collect(Collectors.joining("\n"));
            log.info("Map content/listener:\n{}", mapAsString);
        } catch (Throwable e) {
            throw new IllegalArgumentException("invalid probeModify");
        }

        configServletInner.doPollingConfig(request, response, clientMd5Map, probeModify.length());
    }

    @PostMapping("/remove/config/cache")
    public Result removeConfigCache(@RequestBody Map<String, String> bodyMap) {
        String groupKey = bodyMap.get(Constants.GROUP_KEY);
        if (StrUtil.isNotBlank(groupKey)) {
            ConfigCacheService.removeConfigCache(groupKey);
        }

        return Results.success();
    }

}
