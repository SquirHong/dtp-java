package io.dynamic.threadpool.config.service;

import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Config Servlet Inner.
 */
@Service
@Slf4j
public class ConfigServletInner {

    @Autowired
    private LongPollingService longPollingService;

    public String doPollingConfig(HttpServletRequest request, HttpServletResponse response, Map<String, String> clientMd5Map, int probeRequestSize) {
        log.info("收到客户端长轮询请求，url={},clientMd5Map={},probeRequestSize={}", request.getRequestURI(), clientMd5Map.keySet(), probeRequestSize);
        if (LongPollingService.isSupportLongPolling(request)) {
            longPollingService.addLongPollingClient(request, response, clientMd5Map, probeRequestSize);
            log.info("controller层返回数据");
            return HttpServletResponse.SC_OK + "";
        }
        return HttpServletResponse.SC_OK + "";
    }
}
