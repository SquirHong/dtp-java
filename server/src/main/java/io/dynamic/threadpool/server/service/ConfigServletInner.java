package io.dynamic.threadpool.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Config Servlet Inner.
 */
@Service
public class ConfigServletInner {

    @Autowired
    private LongPollingService longPollingService;

    public String doPollingConfig(HttpServletRequest request, HttpServletResponse response, Map<String, String> clientMd5Map, int probeRequestSize) {
        if (LongPollingService.isSupportLongPolling(request)) {
            longPollingService.addLongPollingClient(request, response, clientMd5Map, probeRequestSize);
            return HttpServletResponse.SC_OK + "";
        }
        return HttpServletResponse.SC_OK + "";
    }
}
