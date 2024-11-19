package io.dynamic.threadpool.config.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.event.Event;
import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.notify.NotifyCenter;
import io.dynamic.threadpool.config.notify.listener.Subscriber;
import io.dynamic.threadpool.config.toolkit.ConfigExecutor;
import io.dynamic.threadpool.config.toolkit.MapUtil;
import io.dynamic.threadpool.config.toolkit.Md5ConfigUtil;
import io.dynamic.threadpool.config.toolkit.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.dynamic.threadpool.common.constant.Constants.GROUP_KEY_DELIMITER;

/**
 * 长轮询服务
 */
@Slf4j
@Service
public class LongPollingService {

    private static final int FIXED_POLLING_INTERVAL_MS = 10000;

    public static final String LONG_POLLING_HEADER = "Long-Pulling-Timeout";

    public static final String CLIENT_APPNAME_HEADER = "Client-AppName";

    private Map<String, Long> retainIps = new ConcurrentHashMap();

    final Queue<ClientLongPolling> allSubs;

    public LongPollingService() {
        allSubs = new ConcurrentLinkedQueue();

        // 记录当前长轮询客户端的数量
        ConfigExecutor.scheduleLongPolling(new StatTask(), 0L, 10L, TimeUnit.SECONDS);

        NotifyCenter.registerToPublisher(LocalDataChangeEvent.class, NotifyCenter.ringBufferSize);

        NotifyCenter.registerSubscriber(new Subscriber() {

            @Override
            public void onEvent(Event event) {
                if (isFixedPolling()) {
                    // Ignore.
                } else {
                    if (event instanceof LocalDataChangeEvent) {
                        LocalDataChangeEvent evt = (LocalDataChangeEvent) event;
                        log.info("接收到配置变更事件，groupKey :: {}", evt.groupKey);
                        ConfigExecutor.executeLongPolling(new DataChangeTask(evt.identify, evt.groupKey));
                    }
                }
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return LocalDataChangeEvent.class;
            }
        });
    }

    public static boolean isSupportLongPolling(HttpServletRequest req) {
        return null != req.getHeader(LONG_POLLING_HEADER);
    }

    private static boolean isFixedPolling() {
        return SwitchService.getSwitchBoolean(SwitchService.FIXED_POLLING, false);
    }

    private static int getFixedPollingInterval() {
        return SwitchService.getSwitchInteger(SwitchService.FIXED_POLLING_INTERVAL, FIXED_POLLING_INTERVAL_MS);
    }

    public void addLongPollingClient(HttpServletRequest req, HttpServletResponse rsp, Map<String, String> clientMd5Map, int probeRequestSize) {
        String str = req.getHeader(LONG_POLLING_HEADER);
        String appName = req.getHeader(CLIENT_APPNAME_HEADER);
        int delayTime = SwitchService.getSwitchInteger(SwitchService.FIXED_DELAY_TIME, 500);
        String noHangUpFlag = req.getHeader(Constants.LONG_PULLING_TIMEOUT_NO_HANGUP);

        long timeout = Math.max(10000, Long.parseLong(str) - delayTime);
        if (isFixedPolling()) {
            timeout = Math.max(10000, getFixedPollingInterval());
        } else {
            List<String> changedGroups = Md5ConfigUtil.compareMd5(req, clientMd5Map);
            if (changedGroups.size() > 0) {
                log.info("配置已经改变，直接返回,不进入异步servlet,changedGroups:{}", changedGroups);
                generateResponse(rsp, changedGroups);
                return;
            } else if (noHangUpFlag != null && noHangUpFlag.equalsIgnoreCase("true")) {
                log.info("no hang up flag is true, no hang up");
                return;
            }
        }

        String ip = RequestUtil.getRemoteIp(req);

        // 异步响应的关键
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0L);
        ConfigExecutor.executeLongPolling(new ClientLongPolling(asyncContext, clientMd5Map, ip, probeRequestSize, timeout, appName));
    }

    public Map<String, Long> getRetainIps() {
        return retainIps;
    }

    /**
     * 统计客户端数量
     */
    class StatTask implements Runnable {

        @Override
        public void run() {
            log.info("[long-pulling] client count " + allSubs.size());
        }
    }

    /**
     * 长轮询的客户端
     */
    class ClientLongPolling implements Runnable {

        final AsyncContext asyncContext;

        final Map<String, String> clientMd5Map;

        final long createTime;

        final String ip;

        final String appName;

        final int probeRequestSize;

        final long timeoutTime;

        Future<?> asyncTimeoutFuture;

        public ClientLongPolling(AsyncContext asyncContext, Map<String, String> clientMd5Map, String ip, int probeRequestSize, long timeout, String appName) {
            this.asyncContext = asyncContext;
            this.clientMd5Map = clientMd5Map;
            this.ip = ip;
            this.probeRequestSize = probeRequestSize;
            this.timeoutTime = timeout;
            this.appName = appName;
            this.createTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            asyncTimeoutFuture = ConfigExecutor.scheduleLongPolling(() -> {
                log.info("------------------超时时间到了---------------------");
                try {
                    getRetainIps().put(ClientLongPolling.this.ip, System.currentTimeMillis());
                    allSubs.remove(ClientLongPolling.this);

                    if (isFixedPolling()) {
                        List<String> changedGroups = Md5ConfigUtil.compareMd5((HttpServletRequest) asyncContext.getRequest(), clientMd5Map);
                        if (changedGroups.size() > 0) {
                            sendResponse(changedGroups);
                        } else {
                            sendResponse(null);
                        }
                    } else {
                        sendResponse(null);
                    }
                } catch (Exception ex) {
                    log.error("Long polling error :: {}", ex.getMessage(), ex);
                }

            }, timeoutTime, TimeUnit.MILLISECONDS);

            allSubs.add(this);
        }

        private void sendResponse(List<String> changedGroups) {

            // Cancel time out task.
            if (null != asyncTimeoutFuture) {
                asyncTimeoutFuture.cancel(false);
            }
            generateResponse(changedGroups);
        }


        private void generateResponse(List<String> changedGroups) {
            if (null == changedGroups) {
                // Tell web container to send http response.
                asyncContext.complete();
                log.info("----------------------------------期间无配置修改------------------------------------------");
                return;
            }

            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            try {
                String respStr = Md5Util.compareMd5ResultString(changedGroups);
                String resultStr = JSON.toJSONString(Results.success(respStr));

                // Disable cache.
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(resultStr);
                asyncContext.complete();
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
                asyncContext.complete();
            }
        }

    }

    class DataChangeTask implements Runnable {

        final String identify;

        final String groupKey;

        DataChangeTask(String identify, String groupKey) {
            this.identify = identify;
            this.groupKey = groupKey;
        }

        @Override
        public void run() {
            try {
                for (Iterator<ClientLongPolling> iter = allSubs.iterator(); iter.hasNext(); ) {
                    ClientLongPolling clientSub = iter.next();

                    String mapAsString = clientSub.clientMd5Map.entrySet().stream().map(entry -> "Key: " + entry.getKey() + ", Value: " + entry.getValue())
                            .collect(Collectors.joining("\n"));
                    log.info("DataChangeTask事件 打印clientMd5Map:\n{},identify:{}", mapAsString, identify);
                    String identity = groupKey + GROUP_KEY_DELIMITER + identify;
                    List<String> parseMapForFilter = Lists.newArrayList(identity);
                    // 为空则修改集群
                    if (StrUtil.isBlank(identify)) {
                        parseMapForFilter = MapUtil.parseMapForFilter(clientSub.clientMd5Map, groupKey);
                    }
                    log.info("DataChangeTask 的 identity:{}", identity);
                    log.info("DataChangeTask事件 parseMapForFilter:{}", parseMapForFilter);
                    parseMapForFilter.forEach(each -> {
                        if (clientSub.clientMd5Map.containsKey(each)) {
                            getRetainIps().put(clientSub.ip, System.currentTimeMillis());

                            ConfigCacheService.updateMd5(each, clientSub.ip, ConfigCacheService.getContentMd5(groupKey));

                            iter.remove();
                            log.info("移除客户端长轮询，groupKey :: {}", groupKey);
                            clientSub.sendResponse(Arrays.asList(groupKey));
                        }
                    });
                }
            } catch (Exception ex) {
                log.error("Data change error :: {}", ex.getMessage(), ex);
            }
        }
    }


    /**
     * 回写响应
     *
     * @param response
     * @param changedGroups
     */
    private void generateResponse(HttpServletResponse response, List<String> changedGroups) {
        if (!CollectionUtils.isEmpty(changedGroups)) {
            try {
                final String changedGroupKeStr = Md5ConfigUtil.compareMd5ResultString(changedGroups);
                final String respString = JSON.toJSONString(Results.success(changedGroupKeStr));
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(respString);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }

}
