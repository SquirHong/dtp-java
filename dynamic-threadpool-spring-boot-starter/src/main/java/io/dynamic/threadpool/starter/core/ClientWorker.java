package io.dynamic.threadpool.starter.core;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.model.PoolParameterInfo;
import io.dynamic.threadpool.common.toolkit.ContentUtil;
import io.dynamic.threadpool.common.toolkit.GroupKey;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.starter.remote.HttpAgent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.dynamic.threadpool.common.constant.Constants.*;

/**
 * 客户端监听工作者
 */
@Slf4j
public class ClientWorker {

    private double currentLongingTaskCount = 0;

    private long timeout;

    private AtomicBoolean isHealthServer = new AtomicBoolean(true);

    private AtomicBoolean isHealthServerTemp = new AtomicBoolean(true);

    private final HttpAgent agent;

    private final String identification;

    private final ScheduledExecutorService executor;

    private final ScheduledExecutorService executorService;

    private final ConcurrentHashMap<String, CacheData> cacheMap = new ConcurrentHashMap(16);

    public ClientWorker(HttpAgent httpAgent, String identification) {
        this.agent = httpAgent;
        this.identification = identification;
        this.timeout = Constants.CONFIG_LONG_POLL_TIMEOUT;

        this.executor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setName("client.Worker.executor");
            t.setDaemon(true);
            return t;
        });

        int threadSize = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newScheduledThreadPool(threadSize, r -> {
            Thread t = new Thread(r);
            // 长轮训
            t.setName("client.Worker.longPolling.executor");
            t.setDaemon(true);
            return t;
        });

        log.info("Client identity :: {}", identification);

        // 1s后，每个任务结束和开始之间的3s，任务内容：检查是否有新的线程配置需要进行长轮训
        this.executor.scheduleWithFixedDelay(() -> {
            try {
                checkConfigInfo();
            } catch (Throwable e) {
                log.error("[sub-check] rotate check error", e);
            }
        }, 1L, 3L, TimeUnit.SECONDS);
    }

    /**
     * 检查配置信息
     */
    public void checkConfigInfo() {
        int listenerSize = cacheMap.size();
        double perTaskConfigSize = 3000D;
        int longingTaskCount = (int) Math.ceil(listenerSize / perTaskConfigSize);
        // 每3000个CacheData 一组
        log.info("当前常轮训数量 currentLongingTaskCount：{},listenerSize：{}", currentLongingTaskCount, listenerSize);
        if (longingTaskCount > currentLongingTaskCount) {
            for (int i = (int) currentLongingTaskCount; i < longingTaskCount; i++) {
                executorService.execute(new LongPollingRunnable());
            }
            currentLongingTaskCount = longingTaskCount;
        }
    }

    /**
     * 长轮训任务
     */
    class LongPollingRunnable implements Runnable {

        @SneakyThrows
        private void checkStatus() {
            if (Objects.equals(isHealthServerTemp.get(), Boolean.FALSE)
                    && Objects.equals(isHealthServer.get(), Boolean.TRUE)) {
                isHealthServerTemp.set(Boolean.TRUE);
                log.info("🚀 The client reconnects to the server successfully.");
            }
            // 服务端状态不正常睡眠 10s
            if (!isHealthServer.get()) {
                isHealthServerTemp.set(Boolean.FALSE);
                log.error("[Check config] Error. exception message, Thread sleep 10 s.");
                Thread.sleep(10000);
                log.info("睡眠十秒结束");
            }
        }

        @SneakyThrows
        @Override
        public void run() {
            checkStatus();

            List<CacheData> cacheDatas = cacheMap.entrySet()
                    .stream().map(each -> each.getValue()).collect(Collectors.toList());
            List<String> inInitializingCacheList = new ArrayList<String>();

            // 开始向服务端发请求检查chacheData是否有更新
            List<String> changedTpIds = checkUpdateDataIds(cacheDatas, inInitializingCacheList);

            if (!CollectionUtils.isEmpty(changedTpIds)) {
                log.info("[dynamic threadPool] tpIds changed :: {}", changedTpIds);
                for (String each : changedTpIds) {
                    String[] keys = StrUtil.split(each, Constants.GROUP_KEY_DELIMITER);
                    // 服务器配置信息
                    String tpId = keys[0];
                    String itemId = keys[1];
                    String tenantId = keys[2];

                    try {
                        String content = getServerConfig(tenantId, itemId, tpId, 3000L);
                        CacheData cacheData = cacheMap.get(tpId);
                        // 这里nacos的源码中，nacos的配置content是可以为空的，但是这里的content为空时，程序会挂掉
                        String poolContent = ContentUtil.getPoolContent(JSON.parseObject(content, PoolParameterInfo.class));
                        cacheData.setContent(poolContent);
                        log.info("[data-received] tenantId :: {}, itemId :: {}, tpId :: {}, md5 :: {}", tenantId, itemId, tpId, cacheData.getMd5());
                    } catch (Exception ex) {
                        log.error("[data-received] Error. Service Unavailable :: {}", ex.getMessage());
                    }
                }

            }

            for (CacheData cacheData : cacheDatas) {
                if (!cacheData.isInitializing() || inInitializingCacheList
                        .contains(GroupKey.getKeyTenant(cacheData.tpId, cacheData.itemId, cacheData.tenantId))) {
                    cacheData.checkListenerMd5();
                    cacheData.setInitializing(false);
                }
            }

            inInitializingCacheList.clear();
            executorService.execute(this);
        }
    }


    /**
     * 转换入参
     *
     * @param cacheDataList
     * @return
     */
    private List<String> checkUpdateDataIds(List<CacheData> cacheDataList, List<String> inInitializingCacheList) {
        StringBuilder sb = new StringBuilder();
        for (CacheData cacheData : cacheDataList) {
            sb.append(cacheData.tpId).append(WORD_SEPARATOR);
            sb.append(cacheData.itemId).append(WORD_SEPARATOR);
            sb.append(cacheData.tenantId).append(WORD_SEPARATOR);
            sb.append(identification).append(WORD_SEPARATOR);
            sb.append(cacheData.getMd5()).append(LINE_SEPARATOR);

            if (cacheData.isInitializing()) {
                // cacheData 首次出现在cacheMap中&首次check更新
                inInitializingCacheList
                        .add(GroupKey.getKeyTenant(cacheData.tpId, cacheData.itemId, cacheData.tenantId));
            }
        }
        boolean isInitializingCacheList = !inInitializingCacheList.isEmpty();
        log.info("[check-update] checkUpdateDataIds :: {}", sb);
        return checkUpdateTpIds(sb.toString(), isInitializingCacheList);
    }

    /**
     * 检查修改的线程池 ID
     */
    public List<String> checkUpdateTpIds(String probeUpdateString, boolean isInitializingCacheList) {
        // 0.75 factor
        Map<String, String> params = new HashMap(2);
        params.put(Constants.PROBE_MODIFY_REQUEST, probeUpdateString);
        Map<String, String> headers = new HashMap(2);
        headers.put(Constants.LONG_PULLING_TIMEOUT, "" + timeout);

        // 确认客户端身份, 修改线程池配置时可单独修改
        headers.put(LONG_PULLING_CLIENT_IDENTIFICATION, identification);

        // told server do not hang me up if new initializing cacheData added in
        if (isInitializingCacheList) {
            headers.put("Long-Pulling-Timeout-No-Hangup", "true");
        }

        if (StringUtils.isEmpty(probeUpdateString)) {
            return Collections.emptyList();
        }
        try {
            long readTimeoutMs = timeout + (long) Math.round(timeout >> 1);
            Result result = agent.httpPostByConfig(Constants.LISTENER_PATH, headers, params, readTimeoutMs);

            setHealthServer(true);

            if (result != null && result.isSuccess()) {
                setHealthServer(true);
                return parseUpdateDataIdResponse(result.getData().toString());
            }
        } catch (Exception ex) {
            setHealthServer(false);
            log.error("[check-update] get changed dataId exception. error message :: {}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    /**
     * 获取服务端配置
     */
    public String getServerConfig(String tenantId, String itemId, String tpId, long readTimeout) {
        Map<String, String> params = new HashMap(8);
        if ("null".equals(tenantId)) {
            tenantId = null;
        }
        params.put("tenantId", tenantId);
        params.put("itemId", itemId);
        params.put("tpId", tpId);
        log.info("[get-server-config] tenantId :: {}, itemId :: {}, tpId :: {}, readTimeOut :: {}", tenantId, itemId, tpId, readTimeout);
        Result result = agent.httpGetByConfig(Constants.CONFIG_CONTROLLER_PATH, null, params, readTimeout);
        log.info("[get-server-config] result :: {}", result);
        if (result.isSuccess() && result.getData() != null) {
            return result.getData().toString();
        }

        log.error("[sub-server-error] tenantId :: {}, itemId :: {}, tpId :: {}, result code :: {}", tenantId, itemId, tpId, result.getCode());
        return Constants.NULL;
    }

    /**
     * Http 响应中获取变更的配置项
     *
     * @param response
     * @return
     */
    public List<String> parseUpdateDataIdResponse(String response) {
        if (StringUtils.isEmpty(response)) {
            return Collections.emptyList();
        }
        try {
            response = URLDecoder.decode(response, "UTF-8");
        } catch (Exception e) {
            log.error("[polling-resp] decode modifiedDataIdsString error", e);
        }
        List<String> updateList = new LinkedList();
        for (String dataIdAndGroup : response.split(LINE_SEPARATOR)) {
            if (!StringUtils.isEmpty(dataIdAndGroup)) {
                String[] keyArr = dataIdAndGroup.split(WORD_SEPARATOR);
                String dataId = keyArr[0];
                String group = keyArr[1];
                if (keyArr.length == 2) {
                    updateList.add(GroupKey.getKey(dataId, group));
                    log.info("[polling-resp] config changed. dataId={}, group={}", dataId, group);
                } else if (keyArr.length == 3) {
                    String tenant = keyArr[2];
                    updateList.add(GroupKey.getKeyTenant(dataId, group, tenant));
                    log.info("[polling-resp] config changed. dataId={}, group={}, tenant={}", dataId, group, tenant);
                } else {
                    log.error("[polling-resp] invalid dataIdAndGroup error {}", dataIdAndGroup);
                }
            }
        }
        return updateList;
    }

    /**
     * CacheData 添加 Listener
     *
     * @param tenantId
     * @param itemId
     * @param tpId
     * @param listeners
     */
    public void addTenantListeners(String tenantId, String itemId, String tpId, List<? extends Listener> listeners) {
        CacheData cacheData = addCacheDataIfAbsent(tenantId, itemId, tpId);
        for (Listener listener : listeners) {
            cacheData.addListener(listener);
        }
    }

    /**
     * CacheData 不存在则添加
     *
     * @param tenantId
     * @param itemId
     * @param tpId
     * @return
     */
    public CacheData addCacheDataIfAbsent(String tenantId, String itemId, String tpId) {
        CacheData cacheData = cacheMap.get(tpId);
        if (cacheData != null) {
            log.info("cacheDatas中已存在CacheData. tpId = {}", tpId);
            return cacheData;
        }

        cacheData = new CacheData(tenantId, itemId, tpId);
        log.info("[Cache Data] Add CacheData. content = {},md5 = {}", cacheData.content, cacheData.md5);
        CacheData lastCacheData = cacheMap.putIfAbsent(tpId, cacheData);
        if (lastCacheData == null) {
            // 插入成功
            String serverConfig = null;
            try {
                serverConfig = getServerConfig(tenantId, itemId, tpId, 3000L);
                PoolParameterInfo poolInfo = JSON.parseObject(serverConfig, PoolParameterInfo.class);
                cacheData.setContent(ContentUtil.getPoolContent(poolInfo));
            } catch (Exception ex) {
                log.error("[Cache Data] Error. Service Unavailable :: {}", ex.getMessage());
            }

            int taskId = cacheMap.size() / Constants.CONFIG_LONG_POLL_TIMEOUT;
            cacheData.setTaskId(taskId);

            lastCacheData = cacheData;
        }

        return lastCacheData;
    }


    public boolean isHealthServer() {
        return this.isHealthServer.get();
    }

    private void setHealthServer(boolean isHealthServer) {
        this.isHealthServer.set(isHealthServer);
    }


}
