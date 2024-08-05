package io.dynamic.threadpool.config.model;

import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.toolkit.Md5Util;
import io.dynamic.threadpool.config.toolkit.SimpleReadWriteLock;
import io.dynamic.threadpool.config.toolkit.SingletonRepository;
import lombok.Getter;
import lombok.Setter;

/**
 * Cache Item.
 */
@Getter
@Setter
public class CacheItem {

    final String groupKey;

    public volatile String md5 = Constants.NULL;

    public volatile long lastModifiedTs;

    public volatile ConfigAllInfo configAllInfo;

    /**
     * 读写锁
     */
    public SimpleReadWriteLock rwLock = new SimpleReadWriteLock();

    public CacheItem(String groupKey) {
        this.groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
    }

    public CacheItem(String groupKey, String md5) {
        this.groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
        this.md5 = md5;
    }

    public CacheItem(String groupKey, ConfigAllInfo configAllInfo) {
        this.groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
        this.configAllInfo = configAllInfo;
        this.md5 = Md5Util.getTpContentMd5(configAllInfo);
    }
}
