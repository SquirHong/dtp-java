package io.dynamic.threadpool.config.service;

import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.config.service.biz.ConfigService;
import io.dynamic.threadpool.config.event.LocalDataChangeEvent;
import io.dynamic.threadpool.config.model.CacheItem;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.notify.NotifyCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Config Cache Service.
 */
@Slf4j
public class ConfigCacheService {

    static ConfigService configService = null;

    private static final ConcurrentHashMap<String, CacheItem> CACHE = new ConcurrentHashMap();

    public static boolean isUpdateData(String groupKey, String md5, String ip) {
        String contentMd5 = ConfigCacheService.getContentMd5IsNullPut(groupKey, ip);
        return Objects.equals(contentMd5, md5);
    }

    /**
     * 获取 Md5
     * TODO：加入 IP, 不同 IP 的线程池区别改写
     *
     * @param groupKey
     * @param ip
     * @return
     */
    private static String getContentMd5IsNullPut(String groupKey, String ip) {
        CacheItem cacheItem = CACHE.get(groupKey);
        if (cacheItem != null) {
            return cacheItem.md5;
        }
        log.info("服务端缓存中没有找到对应的配置，将从数据库中获取配置并计算 MD5 值---groupKey:{}", groupKey);
        if (configService == null) {
            configService = ApplicationContextHolder.getBean(ConfigService.class);
        }
        String[] split = groupKey.split("\\+");
        if (split.length == 3) {
            if ("null".equals(split[2])) {
                split[2] = "";
            }
        }
        ConfigAllInfo config = configService.findConfigAllInfo(split[0], split[1], split[2]);
        if (config != null && !StringUtils.isEmpty(config.getTpId())) {
            String md5 = Md5Util.getTpContentMd5(config);
            log.info("计算 MD5 值成功---groupKey:{}, md5:{}", groupKey, md5);
            cacheItem = new CacheItem(groupKey, md5);
            CACHE.put(groupKey, cacheItem);
        }
        return (cacheItem != null) ? cacheItem.md5 : Constants.NULL;
    }

    public static String getContentMd5(String groupKey) {
        if (configService == null) {
            configService = ApplicationContextHolder.getBean(ConfigService.class);
        }

        String[] split = groupKey.split("\\+");
        ConfigAllInfo config = configService.findConfigAllInfo(split[0], split[1], split[2]);
        if (config == null || StringUtils.isEmpty(config.getTpId())) {
            String errorMessage = String.format("config is null. tpId :: %s, itemId :: %s, tenantId :: %s", split[0], split[1], split[2]);
            throw new RuntimeException(errorMessage);
        }

        return Md5Util.getTpContentMd5(config);
    }

    public static void updateMd5(String groupKey, String md5, long lastModifiedTs) {
        CacheItem cache = makeSure(groupKey);
        if (cache.md5 == null || !cache.md5.equals(md5)) {
            cache.md5 = md5;
            cache.lastModifiedTs = lastModifiedTs;
            NotifyCenter.publishEvent(new LocalDataChangeEvent(groupKey));
        }
    }

    static CacheItem makeSure(final String groupKey) {
        CacheItem item = CACHE.get(groupKey);
        if (null != item) {
            return item;
        }
        CacheItem tmp = new CacheItem(groupKey);
        item = CACHE.putIfAbsent(groupKey, tmp);
        return (null == item) ? tmp : item;
    }

}
