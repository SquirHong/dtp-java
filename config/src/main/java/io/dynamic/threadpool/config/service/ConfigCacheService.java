package io.dynamic.threadpool.config.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.model.CacheItem;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.notify.NotifyCenter;
import io.dynamic.threadpool.config.service.biz.ConfigService;
import io.dynamic.threadpool.config.toolkit.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Config Cache Service.
 */
@Slf4j
public class ConfigCacheService {

    static ConfigService configService = null;

    //   groupKey , ip , CacheItem
    private static final ConcurrentHashMap<String, Map<String, CacheItem>> CACHE = new ConcurrentHashMap();

    public static boolean isUpdateData(String groupKey, String md5, String ip) {
        String contentMd5 = ConfigCacheService.getContentMd5IsNullPut(groupKey, ip);
        return Objects.equals(contentMd5, md5);
    }

    /**
     * 获取 Md5
     *
     * @param groupKey
     * @param ip
     * @return
     */
    private static String getContentMd5IsNullPut(String groupKey, String ip) {
        Map<String, CacheItem> cacheItemMap = Optional.ofNullable(CACHE.get(groupKey)).orElse(Maps.newHashMap());
        CacheItem cacheItem = cacheItemMap.get(ip);
        if (CollUtil.isNotEmpty(cacheItemMap) && cacheItem != null) {
            return cacheItem.md5;
        }
        log.info("服务端缓存中没有找到对应的配置，将从数据库中获取配置并计算 MD5 值---groupKey:{}", groupKey);
        if (configService == null) {
            configService = ApplicationContextHolder.getBean(ConfigService.class);
        }
        String[] params = groupKey.split("\\+");
        ConfigAllInfo config = configService.findConfigRecentInfo(params);
        if (config != null && !StringUtils.isEmpty(config.getTpId())) {
            cacheItem = new CacheItem(groupKey, config);
            cacheItemMap.put(ip, cacheItem);
            CACHE.put(groupKey, cacheItemMap);
        }
        // TODO: 2024/6/30 nacos的配置是以控制台为准，即使客户端使用服务端未定义的groupkey，仍然进行长轮训，但当前业务更适用于C、S两端都可以定义groupkey。
        return (cacheItem != null) ? cacheItem.md5 : Constants.NULL;
    }

    public static String getContentMd5(String groupKey) {
        if (configService == null) {
            configService = ApplicationContextHolder.getBean(ConfigService.class);
        }

        String[] params = groupKey.split("\\+");
        ConfigAllInfo config = configService.findConfigRecentInfo(params);
        if (config == null || StringUtils.isEmpty(config.getTpId())) {
            String errorMessage = String.format("config is null. tpId :: %s, itemId :: %s, tenantId :: %s", params[0], params[1], params[2]);
            throw new RuntimeException(errorMessage);
        }

        return Md5Util.getTpContentMd5(config);
    }

    public static void updateMd5(String groupKey, String ip, String md5) {
        CacheItem cache = makeSure(groupKey, ip);
        if (cache.md5 == null || !cache.md5.equals(md5)) {
            cache.md5 = md5;
            String[] params = groupKey.split("\\+");
            ConfigAllInfo config = configService.findConfigRecentInfo(params);
            cache.configAllInfo = config;
            cache.lastModifiedTs = System.currentTimeMillis();
            NotifyCenter.publishEvent(new LocalDataChangeEvent(ip, groupKey));
        }
    }

    static CacheItem makeSure(final String groupKey, String ip) {
        Map<String, CacheItem> ipCacheItemMap = CACHE.get(groupKey);
        CacheItem item = ipCacheItemMap.get(ip);
        if (null != item) {
            return item;
        }
        CacheItem tmp = new CacheItem(groupKey);
        Map<String, CacheItem> cacheItemMap = Maps.newHashMap();
        cacheItemMap.put(ip, tmp);
        CACHE.putIfAbsent(groupKey, cacheItemMap);
        return tmp;
    }

    /**
     * 后续优化
     * @param identification
     * @return
     */
    public static Map<String, CacheItem> getContent(String identification) {
        List<String> identificationList = MapUtil.parseMapForFilter(CACHE, identification);
        Map<String, CacheItem> returnStrCacheItemMap = Maps.newHashMap();
        identificationList.forEach(each -> returnStrCacheItemMap.putAll(CACHE.get(each)));
        return returnStrCacheItemMap;
    }

    /**
     * Remove config cache.
     *
     * @param groupKey 项目+租户+IP
     */
    public synchronized static void removeConfigCache(String groupKey) {
        // 模糊搜索
        List<String> identificationList = MapUtil.parseMapForFilter(CACHE, groupKey);
        for (String cacheMapKey : identificationList) {
            Map<String, CacheItem> removeCacheItem = CACHE.remove(cacheMapKey);
            log.info("Remove invalidated config cache. config info :: {}", JSON.toJSONString(removeCacheItem));
        }
    }

}
